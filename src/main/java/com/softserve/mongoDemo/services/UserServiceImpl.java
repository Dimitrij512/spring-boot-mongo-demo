package com.softserve.mongoDemo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.softserve.mongoDemo.models.User;
import com.softserve.mongoDemo.repositories.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository repository;


    @Override public User createUser(User user) {
        return repository.insert(user);
    }

    @Override public User updateUser(User user) {
        return repository.save(user);
    }

    @Override public List<User> findAll() {
        return repository.findAll();
    }

    @Override public void deleteUser(String id) {
        repository.deleteById(id);
    }
}
