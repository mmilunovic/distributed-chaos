package org.example.reacurringTasks;

import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.MessageService;
import org.example.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatusCheck{

    @Autowired
    MessageService messageService;

    @Autowired
    DatabaseService databaseService;

    @Scheduled(fixedDelay = 1000, initialDelay = 1000)
    public void statusCheck(){
        Node successor = databaseService.getSuccessor();

        Boolean weakCheckResult = messageService.sendPing(successor, successor,  1);                             // Send weak check, 1000ms

        if(weakCheckResult == null || weakCheckResult == false){

            Node predecessor = databaseService.getPredecessor();

            System.out.println("Node: " + databaseService.getInfo().getID() + " is doing ping for " + "Succ: " + successor.getID() + " Predecessor: " + predecessor.getID());
            Boolean strongCheckResult = messageService.sendPing(successor, predecessor, 10);                      // Send strong check, 10000ms

            if(strongCheckResult == null || strongCheckResult == false){

                databaseService.removeNode(successor);

                messageService.broadcastNodeLeft(successor);
                messageService.sendBootstrapLeft(successor);

                // TODO:
                /*
                predecessorTable.reconstructTable();
                successorTable.reconstructTable();

                nodeService.restructure();
                */
            }
        }
    }
}
