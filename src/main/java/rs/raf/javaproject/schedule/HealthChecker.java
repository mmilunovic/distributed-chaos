package rs.raf.javaproject.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.model.PredecessorTable;
import rs.raf.javaproject.model.SuccessorTable;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;
import rs.raf.javaproject.service.NodeService;

@Component
public class HealthChecker {

    @Autowired
    Database database;

    @Autowired
    MessageService messageService;

    @Autowired
    NodeService nodeService;

    @Autowired
    PredecessorTable predecessorTable;

    @Autowired
    SuccessorTable successorTable;

    @Scheduled(fixedDelay = 1000)
    public void check(){
        Node successor = database.getSuccessor();

        Boolean pingResult = messageService.sendPing(successor, successor, 1);

        if(pingResult == null || pingResult == false){
            Node predecessor = database.getPredecessor();
            Boolean longPingResult = messageService.sendPing(predecessor, successor, 10);

            System.out.println("Long ping:" + longPingResult);
            if(longPingResult == null || longPingResult == false){

                // TODO: mozda je najlakse da sam sebi posalje poruku da je crko - dupliran kod, tamo i ovde
                database.getAllNodes().remove(successor.getId());

                predecessorTable.reconstructTable();
                successorTable.reconstructTable();

                messageService.broadcastLeaveMessage(successor);
                messageService.sendBootstrapLeft(successor);

                //nodeService.restructure();
            }
        }

    }
}
