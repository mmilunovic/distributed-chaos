package org.example.service;

import org.example.model.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JobService {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    public Collection<Job> getAllJobs() {
        return databaseService.getAllJobs();
    }

    public void startJob(Job job) {
        if(!databaseService.getAllJobs().contains(job)){
            databaseService.saveJob(job);
            messageService.broadcastStartJob(job, databaseService.getMyBroadcastingNodes());
        }
    }
}
