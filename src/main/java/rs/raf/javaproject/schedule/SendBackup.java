package rs.raf.javaproject.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.service.MessageService;

import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
public class SendBackup implements Runnable{

    private final long BACKUP_SLEEP = 5000;

    private Database database;
    private MessageService messageService;

    @Override
    public void run() {
        while(true){
            sleep(BACKUP_SLEEP);
            sendBackup();
        }
    }

    private void sendBackup(){
        Node predecessor = database.getPredecessor();
        Node successor = database.getSuccessor();

        String jobID = database.getRegion().getJob().getId();
        String regionID = database.getRegion().getFullID();

        List<Point> data = database.getData();

        BackupInfo newBackup = new BackupInfo(jobID, regionID, data, LocalTime.now());

        Boolean successorDone = messageService.sendSaveBackup(successor, newBackup);
        Boolean predecessorDone = messageService.sendSaveBackup(predecessor, newBackup);

        System.out.println("Backup to successor: " + successorDone.toString() +
                           "\nBackup to predecessor: " + predecessorDone.toString());
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
}
