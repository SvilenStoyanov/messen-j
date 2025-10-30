package com.svistoyanov.mj;

import com.svistoyanov.mj.entity.User;

import java.util.List;

public interface UserDao extends CrudDao<User> {

    List<User> loadByName(String name, int page, int size);

    User loadByEmail(String email);
}
