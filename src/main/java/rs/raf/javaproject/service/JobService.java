package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import java.awt.*;
import java.util.Collection;

@Service
public class JobService {

    @Autowired
    public Database repository;

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
        // TODO: Dodaj posao u svoju listu poslova
        // TODO: Broadcastuje poruku pomocu /api/jobs/start
        // TODO: Reorganizuje svoj posao
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
    }

    public void deleteJob(String jobID){
        // TODO: Samo zaustavimo taj posao na sebi
    }

}
