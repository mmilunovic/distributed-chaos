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

    @Autowired
    MessageService messageService;


    public Node getInfo() {
        return databaseService.getInfo();
    }

    public Collection<Node> allNodes() {
        return databaseService.getAllNodes();
    }

    public Boolean ping(String nodeID) {
        if (!databaseService.getInfo().equals(nodeID)) {
            Boolean pingNodeResult = messageService.sendPing(nodeID, 1);

            if (pingNodeResult == null || pingNodeResult == Boolean.FALSE) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }
}
