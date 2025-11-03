package com.svistoyanov.mj.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author svilen on 03/11/2025
 */
public interface BaseRepository<T, UUID> extends PagingAndSortingRepository<T, UUID>, CrudRepository<T, UUID> {
}
