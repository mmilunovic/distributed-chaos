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
    public Database database;

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
        if(!database.getAllJobs().containsKey(job.getId())){

            database.getAllJobs().put(job.getId(), job);

            messageService.broadcastNewJob(job);

            // TODO: Broadcastuje poruku pomocu /api/jobs/start
            //nodeService.restructure();
        }
    }

    public ResultResponse result(String jobID){
        List<String> receiverIDs = RegionUtil.getAllJobNodeIDs(database.getAllJobs().get(jobID));

        // TODO: Salji pomocu "Chorda"
        // TODO: Nacrtaj jebeni png

        ResultResponse resultResponse = messageService.sendGetResult(jobID, receiverIDs);

        return resultResponse;
    }

    public ResultResponse result(String jobID, String regionID){

        return null;
    }

    public Collection<Point> myWork(String nodeID, String jobID){
        Set<Point> myResult = new HashSet<>();

        if(database.getInfo().getMyRegion().getJob().getId().equals(jobID)){
            myResult.addAll(database.getData());                                            // Dodajemo svoj deo posla
        }
        for(BackupInfo backupInfo : database.getBackups().values()){
            if(backupInfo.getJobID().equals(jobID)){
                myResult.addAll(backupInfo.getData());                                  // Dodajemo bakcup za taj posao ako ga imamo
            }
        }
        return myResult;
    }

    public void stopAll(String jobID){
        // TODO: Zaustavljamo izracunavanje naseg dela posla i saljemo poruku dalje pomocu DELETE /api/jobs/{jobID}
        this.deleteJob(jobID);
    }

    public void deleteJob(String jobID){
        database.getAllJobs().remove(jobID);
        nodeService.restructure();
    }

}
