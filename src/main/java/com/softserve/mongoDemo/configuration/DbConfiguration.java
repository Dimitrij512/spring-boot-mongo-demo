package com.softserve.mongoDemo.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories("com.softserve.mongoDemo.repositories")
@PropertySource("classpath:datasource.properties")
public class DbConfiguration { }
