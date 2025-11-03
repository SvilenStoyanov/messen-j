package com.svistoyanov.mj.repo.impl;

import com.svistoyanov.mj.UserDao;
import com.svistoyanov.mj.entity.User;
import com.svistoyanov.mj.repo.UserRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

public class UserDaoImpl extends CrudDaoImpl<User, UserRepository> implements UserDao {

    public UserDaoImpl(UserRepository repository) {
        super(repository);
    }

    @Override
    public List<User> loadByName(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("username"));
        return repository.loadByName(name, pageable);
    }

    @Override
    public User loadByEmail(String email) {
        return repository.loadByEmail(email).orElse(null);
    }
}
