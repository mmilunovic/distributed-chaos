package rs.raf.javaproject.service;

import rs.raf.javaproject.model.IJob;
import rs.raf.javaproject.repository.IRepository;
import rs.raf.javaproject.repository.InMemoryDatabase;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import java.awt.*;
import java.util.Collection;

public class JobService {

    public IRepository repository;

    public JobService(){
        repository = InMemoryDatabase.getInstance();
    }

    public StatusResponse status(){
        return null;
    }

    public StatusResponse status(String jobID){
        return null;
    }

    public StatusResponse status(String jobID, String regionID){
        return null;
    }

    public void start(IJob job){
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
