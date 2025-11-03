package com.svistoyanov.mj.dao;

import com.svistoyanov.mj.CrudDao;
import com.svistoyanov.mj.DaoProvider;
import com.svistoyanov.mj.DaoProviderFactory;
import com.svistoyanov.mj.PersistenceTestHelper;
import com.svistoyanov.mj.entity.AbstractEntity;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.UUID;

public abstract class AbstractCrudDaoTests<EntityType extends AbstractEntity> {
    protected final DaoProviderFactory daoProviderFactory = PersistenceTestHelper.getDaoProviderFactory();

    @AfterEach
    public void tearDown() {
        PersistenceTestHelper.resetAllTables();
    }

    @Test
    public void testInsertEntity() {
        EntityType expectedEntity = runSave(daoProviderFactory);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            assertDeepEquals(expectedEntity, getDao(daoProvider).loadById(expectedEntity.getId()));
        }
    }

    @Test
    public void testUpdate() {
        EntityType entityType = runSave(daoProviderFactory);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            entityType = getDao(daoProvider).loadById(entityType.getId());
            modifiedEntity(entityType);
            getDao(daoProvider).saveOrUpdate(entityType);
            daoProvider.commit();
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            EntityType actualEntity = getDao(daoProvider).loadById(entityType.getId());
            assertDeepEquals(entityType, actualEntity);
        }
    }

    @Test
    public void testEntityLoadByInvalidId() {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(IllegalArgumentException.class, () -> getDao(daoProvider).loadById(null));
        }
    }

    @Test
    void testDelete() {
        final EntityType savedEntity = runSave(daoProviderFactory);
        delete(daoProviderFactory, savedEntity.getId());
    }

    @Test
    public void testDeleteNonExistingEntity() {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            UUID uuid = UUID.randomUUID();
//            Assertions.assertThrows(EmptyResultDataAccessException.class, () ->
//            {
                getDao(daoProvider).delete(uuid);
                daoProvider.commit();
//            });

            EntityType entityType = getDao(daoProvider).loadById(uuid);
            Assertions.assertNull(entityType);
        }
    }

    @Test
    public void throwsExceptionWhenTryingToDeleteEntityUsingNullId() {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(IllegalArgumentException.class, () ->
            {
                getDao(daoProvider).delete(null);
                daoProvider.commit();
            });
        }
    }

    @Test
    public void testExistingEntityLoadById() {
        final EntityType expected = runSave(daoProviderFactory);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            EntityType actual = getDao(daoProvider).loadById(expected.getId());
            Assertions.assertEquals(expected.getId(), actual.getId());
            Assertions.assertNotNull(actual);
        }
    }

    @Test
    public void testNonExistingEntityLoadById() {
        assertNA(daoProviderFactory, UUID.randomUUID());
    }

    public void delete(DaoProviderFactory daoProviderFactory, UUID id) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            getDao(daoProvider).delete(id);
            daoProvider.commit();
        }
        assertNA(daoProviderFactory, id);
    }

    public EntityType runSave(DaoProviderFactory daoProviderFactory) {
        final EntityType expectedEntity = createValidEntity();
        final EntityType dbEntity = saveOrUpdate(daoProviderFactory, expectedEntity);

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            assertDeepEquals(expectedEntity, getDao(daoProvider).loadById(dbEntity.getId()));
            return dbEntity;
        }
    }

    public EntityType saveOrUpdate(DaoProviderFactory daoProviderFactory, EntityType entity) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            getDao(daoProvider).saveOrUpdate(entity);
            daoProvider.commit();
        }

        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            final EntityType dbEntity = getDao(daoProvider).loadById(entity.getId());
            Assertions.assertNotNull(dbEntity, "missing entity");
            assertDeepEquals(entity, dbEntity);
            return dbEntity;
        }
    }

    public void assertNA(DaoProviderFactory daoProviderFactory, UUID id) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertNull(getDao(daoProvider).loadById(id));
        }
    }

    protected void testInvalidSave(EntityType entityType) {
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            Assertions.assertThrows(ConstraintViolationException.class, () ->
            {
                getDao(daoProvider).saveOrUpdate(entityType);
                daoProvider.commit();
            });
        }
        try (DaoProvider daoProvider = daoProviderFactory.createDaoProvider()) {
            EntityType actualEntity = getDao(daoProvider).loadById(entityType.getId());
            Assertions.assertNull(actualEntity);
        }
    }

    protected abstract EntityType createValidEntity();

    protected abstract CrudDao<EntityType> getDao(DaoProvider daoProvider);

    protected abstract void assertDeepEquals(EntityType expected, EntityType actual);

    protected abstract void modifiedEntity(EntityType expected);

}
