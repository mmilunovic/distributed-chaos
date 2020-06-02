package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import java.util.*;

@Service
public class JobService {

    @Autowired
    public Database repository;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MessageService messageService;

    public StatusResponse status(){
        return null;
    }

    public StatusResponse status(String jobID){
        return null;
    }

    public StatusResponse status(String jobID, String regionID){
        return null;
    }

    public void start(Job job){
        if(!repository.getAllJobs().containsKey(job.getId())){

            repository.getAllJobs().put(job.getId(), job);

            messageService.broadcastNewJob(job);

            // TODO: Broadcastuje poruku pomocu /api/jobs/start
            //nodeService.restructure();
        }



    }

    public ResultResponse result(String jobID){
        return null;
    }

    public ResultResponse result(String jobID, String regionID){
        return null;
    }

    public Collection<Point> myWork(String nodeID, String jobID){
        // TODO: Uzmemo svoj deo posla za jobID ili backupe ako smo ih cuvali
        return null;
    }

    public void stopAll(String jobID){
        // TODO: Zaustavljamo izracunavanje naseg dela posla i saljemo poruku dalje pomocu DELETE /api/jobs/{jobID}
        this.deleteJob(jobID);
    }

    public void deleteJob(String jobID){
        repository.getAllJobs().remove(jobID);
        nodeService.restructure();
    }

}
