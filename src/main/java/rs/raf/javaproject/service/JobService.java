package rs.raf.javaproject.service;

import ch.qos.logback.core.pattern.util.RegularEscapeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.response.RegionStatusResponse;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import javax.imageio.ImageIO;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@Service
public class JobService {

    @Autowired
    public Database database;

    @Autowired
    private NodeService nodeService;

    @Autowired
    private MessageService messageService;

    public Collection<StatusResponse> status(){

        Collection<StatusResponse> response = new ArrayList<>();

        ArrayList<String> jobs = null;
        synchronized (database.getInfo()) {
            jobs = new ArrayList<>(database.getAllJobs().keySet());
        }
        for (String jobID: jobs) {
            response.add(status(jobID));
        }

        return response;
    }

    public StatusResponse status(String jobID){
        List<String> receiverIDs = null;
        synchronized (database.getInfo()){
            receiverIDs = RegionUtil.getAllJobNodeIDs(database.getAllJobs().get(jobID));
        }
//
//        System.out.println(receiverIDs);

        StatusResponse statusResponse = messageService.sendGetStatus(jobID, receiverIDs);

        return statusResponse;
    }

    // TODO: Fix
    public RegionStatusResponse status(String jobID, String regionID){
        List<String> receiverIDs = RegionUtil.getAllSubregionNodeIDs(database.getAllJobs().get(jobID).getRegions().get(regionID));

        RegionStatusResponse response = new RegionStatusResponse();
        for (String recever: receiverIDs){
            RegionStatusResponse regionStatusResponse = myStatus(recever, jobID, regionID);
        }

        return null;
    }


    public RegionStatusResponse myStatus(String nodeID, String jobID){
        RegionStatusResponse regionStatusResponse = new RegionStatusResponse();

        synchronized (database.getInfo()) {
            if (nodeID.equals(database.getInfo().getId())) {
                regionStatusResponse.setNodeID(nodeID);
                regionStatusResponse.setRegionID(database.getRegion().getFullID());
                regionStatusResponse.setNumberOfPoints(database.getData().size());
            } else {
                List<String> recipient = new ArrayList<>();
                recipient.add(nodeID);
                regionStatusResponse = messageService.sendGetRegionStatus(jobID, nodeID);
            }
        }
        // TODO: Da li ovde treba dodati backupe?
        return regionStatusResponse;
    }

    // TODO ovo ne radi
    public RegionStatusResponse myStatus(String nodeID, String jobID, String regionID){
        RegionStatusResponse regionStatusResponse = new RegionStatusResponse();
        System.out.println(database.getInfo().getId() + " dobija status za " + nodeID + " i " + jobID);

        if(nodeID.equals(database.getInfo().getId())){
            regionStatusResponse.setNodeID(nodeID);
            BackupInfo backupInfo = database.getBackups().get(jobID+":"+regionID);
            regionStatusResponse.setRegionID(database.getRegion().getFullID());
            regionStatusResponse.setNumberOfPoints(backupInfo.getData().size());
        }else{
            List<String> recipient = new ArrayList<>();
            recipient.add(nodeID);
            regionStatusResponse = messageService.sendGetRegionStatus(jobID, nodeID, regionID);
        }

        return regionStatusResponse;
    }

    public void start(Job job){
        synchronized (database.getInfo()) {
            if (!database.getAllJobs().containsKey(job.getId())) {

                database.getAllJobs().put(job.getId(), job);

                messageService.broadcastNewJob(job);

                nodeService.restructure();
            }
        }
    }

    public ResultResponse result(String jobID){
        List<String> receiverIDs = null;
        synchronized (database.getInfo()){
            receiverIDs = RegionUtil.getAllJobNodeIDs(database.getAllJobs().get(jobID));
        }

        System.out.println(receiverIDs);
        System.out.println(database.getSuccessor());
        ResultResponse resultResponse = messageService.sendGetResult(jobID, receiverIDs);


        drawResult(resultResponse, jobID);

        return resultResponse;
    }

    public ResultResponse result(String jobID, String regionID){
        List<String> receiverIDs = null;
        synchronized (database.getInfo()){
            receiverIDs = RegionUtil.getAllSubregionNodeIDs(
                    RegionUtil.getRegionFromID(database.getAllJobs(), jobID, regionID));
        }

        ResultResponse resultResponse = messageService.sendGetResult(jobID, regionID, receiverIDs);

        drawResult(resultResponse, jobID);

        return resultResponse;
    }

    public Collection<Point> getRegionResultFromNode(String nodeID, String jobID, String regionID){
        Set<Point> myResult = new HashSet<>();

        synchronized (database.getInfo()) {
            if (nodeID.equals(database.getInfo().getId())) {
                if (database.getInfo().getMyRegion() == null)
                    return new ArrayList<>();

                if (database.getInfo().getMyRegion().getJob().getId().equals(jobID)) {

                    myResult.addAll(database.getData());
                }

            } else {
                List<String> recepient = new ArrayList<>();
                recepient.add(nodeID);

                ResultResponse resultResponse = messageService.sendGetResult(jobID, regionID, recepient);
                myResult.addAll(resultResponse.getData());
            }

            for (BackupInfo backupInfo : database.getBackups().values()) {
                if (backupInfo.getJobID().equals(jobID) && backupInfo.getRegionID().equals(regionID)) {
                    myResult.addAll(backupInfo.getData());                                  // Dodajemo bakcup za taj posao ako ga imamo
                }
            }

        }

        return myResult;
    }
    public Collection<Point> getJobResultFromNode(String nodeID, String jobID){
        Set<Point> myResult = new HashSet<>();

        synchronized (database.getInfo()) {
            if (nodeID.equals(database.getInfo().getId())) {
                if (database.getInfo().getMyRegion() == null)
                    return new ArrayList<>();

                if (database.getInfo().getMyRegion().getJob().getId().equals(jobID)) {
                    myResult.addAll(database.getData());

                }

            } else {
                List<String> recepient = new ArrayList<>();
                recepient.add(nodeID);

                ResultResponse resultResponse = messageService.sendGetResult(jobID, recepient);
                myResult.addAll(resultResponse.getData());
            }

            for (BackupInfo backupInfo : database.getBackups().values()) {
                if (backupInfo.getJobID().equals(jobID)) {
                    myResult.addAll(backupInfo.getData());                                  // Dodajemo bakcup za taj posao ako ga imamo
                }
            }

        }
        return myResult;
    }



    public void stopAll(String jobID){
        // TODO: Zaustavljamo izracunavanje naseg dela posla i saljemo poruku dalje pomocu DELETE /api/jobs/{jobID}
        this.deleteJob(jobID);
    }

    public void deleteJob(String jobID){
        synchronized (database.getInfo()) {
            database.getAllJobs().remove(jobID);
        }
        nodeService.restructure();
    }

    private void drawResult(ResultResponse resultResponse, String jobID){
        ArrayList<Point> resultPoints = resultResponse.getData();
        // TODO: Promeni ovo
        int height = 100;
        int width = 100;

        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.red);
        for(Point point: resultPoints){
            g2d.drawLine((int)Math.round(point.getX()), (int)Math.round(point.getY()),
                    (int)Math.round(point.getX()), (int)Math.round(point.getY()));
        }

        g2d.setColor(Color.blue);
        for(Point point: database.getAllJobs().get(jobID).getStartingPoints()){
            g2d.drawLine((int)Math.round(point.getX()), (int)Math.round(point.getY()),
                    (int)Math.round(point.getX()), (int)Math.round(point.getY()));
        }

        g2d.dispose();

        File file = new File("result"+ jobID + ".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
