package rs.raf.javaproject.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;

import java.time.LocalTime;
import java.util.List;

@Component
public class SendBackup{

    @Autowired
    private Database database;
    @Autowired
    private MessageService messageService;

    //@Scheduled(fixedDelay = 5000, initialDelay = 1000)
    private void sendBackup(){

        if(database.getRegion() != null) {
            Node predecessor = database.getPredecessor();
            Node successor = database.getSuccessor();

            String jobID = database.getRegion().getJob().getId();
            String regionID = database.getRegion().getFullID();

            List<Point> data = database.getData();

            BackupInfo newBackup = new BackupInfo(jobID, regionID, data, LocalTime.now());

            database.getBackups().put(newBackup.getID(), newBackup);

            messageService.sendSaveBackup(successor, newBackup);
            messageService.sendSaveBackup(predecessor, newBackup);
        }
    }

}
