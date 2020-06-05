package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class NodeService {

    @Autowired
    DatabaseService databaseService;


    public Node getInfo() {
        return databaseService.getInfo();
    }

    public Collection<Node> allNodes() {
        return databaseService.getAllNodes();
    }

    public Collection<Job> getAllJobs() {
        return databaseService.getAllJobs();
    }
}
