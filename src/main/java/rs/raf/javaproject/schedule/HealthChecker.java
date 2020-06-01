package rs.raf.javaproject.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;

@Component
public class HealthChecker {

    @Autowired
    Database database;

    @Autowired
    MessageService messageService;

    @Scheduled(fixedDelay = 1000)
    public Boolean check(){
        Node successor = database.getSuccessor();

        Boolean pingResult = messageService.sendPing(successor, successor, 1);

        if(pingResult == null || pingResult == false){
            Node predecessor = database.getPredecessor();
            Boolean longPingResult = messageService.sendPing(predecessor, successor, 10);

            if(longPingResult == null || longPingResult == false){
                return false;
            }
            return true;
        }
        return true;

    }
}
