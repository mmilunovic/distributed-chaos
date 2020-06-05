package org.example.reacurringTasks;

import org.example.model.Backup;
import org.example.model.Job;
import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class BackupData {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    @Scheduled(fixedDelay = 5000, initialDelay = 1000)
    public void backup(){

        // Koji job izvrsavam
        Job job = databaseService.getMyJob();
        if(job != null){ // Ako nisam idle

            Backup backup = new Backup();
            backup.setJobID(job.getId());
            backup.setRegionID(databaseService.getMyRegion().getId());
            backup.setData(databaseService.getCurrentData());

//            System.out.println(backup);

            databaseService.saveBackup(backup);
//            System.out.println(databaseService.getInfo().getID() + " is sending backup to " + databaseService.getSuccessor() + " and " + databaseService.getPredecessor());
            messageService.sendSaveBackup(backup, databaseService.getSuccessor());
            messageService.sendSaveBackup(backup, databaseService.getPredecessor());

        }else{
            System.out.println(databaseService.getInfo().getID() + " is idle and not sending backup");
        }

    }
}
