package org.example.service;

import org.example.JavaServlet;
import org.example.model.Backup;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class NodeService {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    @Autowired
    ReconstructionService reconstructionService;

    @Autowired
    private ConfigurableApplicationContext context;


    public Node getInfo() {
        return databaseService.getInfo();
    }

    public Collection<Node> allNodes() {
        return databaseService.getAllNodes();
    }

    public Boolean ping(String nodeID) {
        if (!databaseService.getInfo().getID().equals(nodeID)) {
            Boolean pingNodeResult = messageService.sendPing(databaseService.getNodeFromID(nodeID), databaseService.getNodeFromID(nodeID), 1);

            if (pingNodeResult == null || pingNodeResult == Boolean.FALSE) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void updateNewNode(Node newNode) {
        databaseService.saveNode(newNode);
        reconstructionService.reconstruct();
    }

    public void broadcastNewNode(Node newNode) {
        if(!databaseService.isKnown(newNode)){
            databaseService.saveNode(newNode);
            messageService.broadcastNewNode(databaseService.getMyBroadcastingNodes(), newNode);
            reconstructionService.reconstruct();
        }
    }

    public void nodeLeft(Node exitingNode) {
        System.out.println(databaseService.getInfo().getID() + " is informing that " + exitingNode.getID() + " is leaving...");

        if(databaseService.isKnown(exitingNode)) {
            databaseService.removeNode(exitingNode);

            System.out.println(databaseService.getInfo().getID() + " is broadcasting that " + exitingNode.getID() + " is leaving...");
            messageService.broadcastNodeLeft(exitingNode);

            reconstructionService.reconstruct();
        }
    }

    public void quit() {
        System.out.println("I'm leaving: " + databaseService.getInfo().getID());
        messageService.sendBootstrapLeft(databaseService.getInfo());
        SpringApplication.exit(context,() -> 0);
    }

    public void saveBackup(Backup backup) {
        //System.out.println("Node " + databaseService.getInfo().getID() + " is saving backup " + backup);
        databaseService.saveBackup(backup);
    }

    public Backup getBackup(Node finalDestination, String jobID, String regionID) {
        System.out.println("Node " + databaseService.getInfo().getID() + " is getting backup for " + jobID +":" + regionID + " from " + finalDestination.getID());
        if(databaseService.getInfo().equals(finalDestination)){
            return databaseService.getBackupForBackupID(jobID + ":" + regionID);
        }else{
            Node delegator = reconstructionService.getDelegatorFromTable(finalDestination);
            return messageService.sendGetBackup(delegator, finalDestination, jobID, regionID);
        }
    }
}
