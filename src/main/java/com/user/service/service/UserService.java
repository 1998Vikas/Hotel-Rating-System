package com.user.service.service;

import com.user.service.entity.User;

import java.util.List;

public interface UserService {
    User saveUser(User user);

    //get all user
    List<User> getAll();

    //get single user using id
    User getUser(String id);
}
