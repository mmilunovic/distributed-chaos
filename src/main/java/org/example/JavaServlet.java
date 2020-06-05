package org.example;


import org.example.config.ServentConfig;
import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.MessageService;
import org.example.service.ReconstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.annotation.PostConstruct;
import java.util.Collection;

@SpringBootApplication
@EnableScheduling
public class JavaServlet {

    @Autowired
    ServentConfig config;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    @Autowired
    ReconstructionService reconstructionService;


    public static void main(String[] args) {
        SpringApplication.run(JavaServlet.class, args);
    }

    @PostConstruct
    public void init() {

        config.setServent(new Node(config.getAddress(), config.getPort()));

        databaseService.saveNode(databaseService.getInfo()); // Dodaje me u kolekciju svih nodova

        Node enteringNode = messageService.sendBootstrapHail();

        if(enteringNode.getIp() == null){
            messageService.sendBootstrapNew(config.getServent());
            reconstructionService.reconstruct();
        }else{

            databaseService.saveNodes(messageService.sendGetAllNodes(enteringNode));
            databaseService.saveJobs(messageService.sendGetAllJobs(enteringNode));

            reconstructionService.reconstruct();

            messageService.sendUpdateNewNode(databaseService.getPredecessor(), databaseService.getInfo());
            messageService.broadcastNewNode(databaseService.getMyBroadcastingNodes(), databaseService.getInfo());

            messageService.sendBootstrapNew(config.getServent());

        }

    }
}
