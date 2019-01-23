package com.softserve.mongoDemo.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.softserve.mongoDemo.models.User;

@Repository
public interface UserRepository extends MongoRepository<User, String> {}
