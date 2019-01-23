package com.softserve.mongoDemo.services;

import java.util.List;

import com.softserve.mongoDemo.models.User;

public interface UserService {
    User createUser(User user);

    User updateUser(User user);

    List<User> findAll();

    void deleteUser(String id);
}
