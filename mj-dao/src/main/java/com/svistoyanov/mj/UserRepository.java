package com.svistoyanov.mj;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository<User, UUID> extends PagingAndSortingRepository<User, UUID>, CrudRepository<User, UUID> {

    @Query( value = "select u from User u where lower(u.username) like lower(concat('%',:username,'%')) ") //%username%
    List<User> loadByName(@Param("username") String username, Pageable pageable);

    @Query("select u from User u where u.email = :email")
    Optional<User> loadByEmail(@Param("email") String email);

}
