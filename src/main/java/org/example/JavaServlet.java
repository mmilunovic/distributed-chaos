package org.example;


import org.example.config.ServentConfig;
import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class JavaServlet {

    @Autowired
    ServentConfig config;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    public static void main(String[] args) {
        SpringApplication.run(JavaServlet.class, args);
    }

    @PostConstruct
    public void init() {

        config.setServent(new Node(config.getAddress(), config.getPort()));

        databaseService.saveNode(databaseService.getInfo()); // Dodaje me u kolekciju svih nodova

        Node enteringNode = messageService.sendBootstrapHail();

        if(enteringNode.getAddress() == null){
            messageService.sendBootstrapNew();
        }else{

        }

    }
}
