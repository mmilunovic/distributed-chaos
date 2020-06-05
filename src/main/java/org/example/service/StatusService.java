package org.example.service;

import org.example.model.Backup;
import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Region;
import org.example.response.StatusResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

@Component
public class StatusService {

    @Autowired
    MessageService messageService;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ReconstructionService reconstructionService;

    public Collection<StatusResponse> getStatus(String jobID, String regionID) {
        Job requestedJob = databaseService.getJobFromID(jobID);

        Region requestedRegion = new Region(regionID);

        Collection<Node> receivers = databaseService.getNodesForJobIDAndRegionID(jobID, regionID);

        return sendGetStatus(receivers);
    }

    public Collection<StatusResponse> getStatus(String jobID) {
        return getStatus(jobID, "-");
    }

    public Collection<StatusResponse> getStatus() {
        ArrayList<StatusResponse> statusResponseList = new ArrayList<>();
        for(Job job : databaseService.getAllJobs()){
            statusResponseList.addAll(getStatus(job.getId()));
        }
        return statusResponseList;
    }

    public Collection<StatusResponse> sendGetStatus(Collection<Node> receivers) {
        Collection<StatusResponse> statusResponseResult = new ArrayList<>();
        for(Node receiver : receivers){
            Job requestedJob = databaseService.getJobFromNode(receiver);
            Region requestedRegion = databaseService.getRegionFromNode(receiver);
            Node delegator = reconstructionService.getDelegatorFromTable(receiver);
            Backup receiverBackup = messageService.sendGetBackup(delegator, receiver, requestedJob.getId(), requestedRegion.getId());

            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setNodeID(receiver.getID());
            statusResponse.setRegionID(databaseService.getRegionFromNode(receiver).getId());
            statusResponse.setNumberofPoints(receiverBackup.getData().size());
            statusResponse.setJobID(requestedJob.getId());
            statusResponseResult.add(statusResponse);
        }

        return statusResponseResult;
    }
}
