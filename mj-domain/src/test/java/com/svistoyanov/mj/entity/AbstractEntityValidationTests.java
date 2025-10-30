package com.svistoyanov.mj.entity;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;

public abstract class AbstractEntityValidationTests<EntityType extends AbstractEntity> {

    protected Validator  validator = Validation.buildDefaultValidatorFactory().getValidator();
    protected EntityType entity;

    @Test
    void shouldCreateEntityWhenValidParameters() {
        Set<ConstraintViolation<EntityType>> actual = validator.validate(entity);
        Assertions.assertEquals(0, actual.size());
    }

    public void testInvalidEntity(EntityType entity, String... expectedMessage) {

        Set<ConstraintViolation<EntityType>> actual = validator.validate(entity);
        Assertions.assertEquals(expectedMessage.length, actual.size());
        Assertions.assertEquals(expectedMessage[0], actual.iterator().next().getMessage());
    }

}
