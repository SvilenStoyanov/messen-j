package com.svistoyanov.mj;

import java.util.UUID;

public interface CrudDao<T> {

    T saveOrUpdate(T t);

    void delete(UUID id);

    T loadById(UUID id);
}
