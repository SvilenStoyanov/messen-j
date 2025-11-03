package com.svistoyanov.mj.repo.impl;

import com.svistoyanov.mj.CrudDao;
import com.svistoyanov.mj.repo.BaseRepository;

import java.util.UUID;

public abstract class CrudDaoImpl<T, Repository extends BaseRepository<T, UUID>> implements CrudDao<T> {

    protected Repository repository;

    protected CrudDaoImpl(Repository repository) {
        this.repository = repository;
    }

    @Override
    public T saveOrUpdate(T t) {
        return repository.save(t);
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public T loadById(UUID id) {
        return repository.findById(id).orElse(null);
    }

}
