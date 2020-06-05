package org.example.service;

import org.example.JavaServlet;
import org.example.model.Backup;
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

    public boolean ping(String nodeID) {
        if (!databaseService.getInfo().getID().equals(nodeID)) {
            Boolean pingNodeResult = messageService.sendPing(databaseService.getNodeFromID(nodeID), databaseService.getNodeFromID(nodeID), 1);

            if (pingNodeResult == null || pingNodeResult == false) {
                return false;
            }
        }
        return true;
    }

    public void updateNewNode(Node newNode) {
        databaseService.saveNode(newNode);
    }

    public void broadcastNewNode(Node newNode) {
        if(!databaseService.isKnown(newNode)){
            databaseService.saveNode(newNode);
            messageService.broadcastNewNode(databaseService.getMyBroadcastingNodes(), newNode);
        }
    }

    public void nodeLeft(Node exitingNode) {
        System.out.println(databaseService.getInfo().getID() + " is informing that " + exitingNode.getID() + " is leaving...");
        System.out.println(databaseService.getAllNodes());
        if(databaseService.getAllNodes().contains(exitingNode)) {
            databaseService.removeNode(exitingNode);
            // TODO:
        /*
        Update successor and predecessor table
        * */
            messageService.broadcastNodeLeft(exitingNode);
        }
    }

    public void quit() {
        // TODO: Ovde je bio synchronized
        System.out.println("I'm leaving: " + databaseService.getInfo().getID());
        //messageService.broadcastNodeLeft(databaseService.getInfo());
        JavaServlet.exitThread();
    }

    public void saveBackup(Backup backup) {
        databaseService.saveBackup(backup);
    }
}
