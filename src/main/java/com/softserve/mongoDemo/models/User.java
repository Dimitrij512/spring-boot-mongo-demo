package com.softserve.mongoDemo.models;

import java.util.List;

import lombok.Data;

@Data
public class User {
    String id;
    String name;
    String lastName;
    int age;
    //boolean testField;
    Contact contact;
    boolean isActive;
    List<Comment> commentsList;
}
