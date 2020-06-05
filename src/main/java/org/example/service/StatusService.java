package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Region;
import org.example.response.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.HashSet;

@Component
public class StatusService {

    @Autowired
    MessageService messageService;

    @Autowired
    DatabaseService databaseService;

    public Collection<StatusResponse> getStatus(String jobID, String regionID) {
        Job requestedJob = databaseService.getJobFromID(jobID);

        Region requestedRegion = new Region(regionID);

        Collection<Node> receivers = databaseService.getNodesForJobIDAndRegionID(jobID, regionID);

        return messageService.sendGetStatus(requestedJob, requestedRegion, receivers);
    }
}
