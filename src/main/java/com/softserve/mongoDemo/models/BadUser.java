package com.softserve.mongoDemo.models;

import java.util.List;

import lombok.Data;

@Data
public class BadUser {
    private String id;
    private String name;
    private String lastName;
    private String city;
    private List<String> comments;
}
