package com.svistoyanov.mj;

import com.svistoyanov.mj.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends BaseRepository<User, UUID> {

    @Query( value = "select u from User u where lower(u.username) like lower(concat('%',:username,'%')) ") //%username%
    List<User> loadByName(@Param("username") String username, Pageable pageable);

    @Query("select u from User u where u.email = :email")
    Optional<User> loadByEmail(@Param("email") String email);

}
