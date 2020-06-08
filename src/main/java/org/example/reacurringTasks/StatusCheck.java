package org.example.reacurringTasks;

import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.MessageService;
import org.example.service.NodeService;
import org.example.service.ReconstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StatusCheck{

    @Autowired
    MessageService messageService;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ReconstructionService reconstructionService;

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void statusCheck(){
        Node successor = databaseService.getSuccessor();

        Boolean weakCheckResult = messageService.sendPing(successor, successor,  1);                             // Send weak check, 1000ms

        if(weakCheckResult == null || weakCheckResult == Boolean.FALSE){

            Node predecessor = databaseService.getPredecessor();

            Boolean strongCheckResult = messageService.sendPing(successor, predecessor, 10);

            if(strongCheckResult == null || strongCheckResult == Boolean.FALSE){

                databaseService.removeNode(successor);

                messageService.broadcastNodeLeft(successor);
                messageService.sendBootstrapLeft(successor);

                reconstructionService.reconstruct();

            }
        }
    }
}
