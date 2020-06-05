package org.example;


import org.example.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class JavaServlet {

    @Autowired
    DatabaseService databaseService;

    public static void main(String[] args) {
        SpringApplication.run(JavaServlet.class, args);
    }

    @PostConstruct
    public void init() {

    }
}
