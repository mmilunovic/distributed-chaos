package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class JobService {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    MessageService messageService;

    @Autowired
    ReconstructionService reconstructionService;

    public Collection<Job> getAllJobs() {
        return databaseService.getAllJobs();
    }

    public void startJob(Job job) {
        if(!databaseService.isKnownJob(job)){
            databaseService.saveJob(job);
            messageService.broadcastStartJob(job, databaseService.getMyBroadcastingNodes());
            reconstructionService.reconstruct();
            System.out.println(databaseService.getMyRegion().getId());

        }
    }

    public void deleteJob(String jobID) {
        if(databaseService.isKnownJob(jobID)){
            databaseService.remoeJob(databaseService.getJobFromID(jobID));
            messageService.sendDeleteJob(jobID);
        }
    }
}
