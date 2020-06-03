package rs.raf.javaproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.raf.javaproject.model.*;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;

import javax.imageio.ImageIO;
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

        for (String jobID: database.getAllJobs().keySet()) {
            response.add(status(jobID));
        }

        return response;
    }

    public StatusResponse status(String jobID){
        Job job = database.getAllJobs().get(jobID);

        return null;
    }

    public StatusResponse status(String jobID, String regionID){
        // TODO: koristiti tabelu sledbenika
        // TODO: https://docs.google.com/document/d/1R8uygEGYILpqh34eT_hjNgH-zQHGPYHe51d061e3e0I/edit#heading=h.w7srezuqw9i5
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

        System.out.println(database.getInfo().getId() + " uzima podatke od " + receiverIDs);
        ResultResponse resultResponse = messageService.sendGetResult(jobID, receiverIDs);

        drawResult(resultResponse, jobID);

        return resultResponse;
    }

    public ResultResponse result(String jobID, String regionID){
        List<String> receiverIDs = RegionUtil.getAllSubregionNodeIDs(
                RegionUtil.getRegionFromID(database.getAllJobs(), jobID, regionID));

        ResultResponse resultResponse = messageService.sendGetResult(jobID, receiverIDs);

        drawResult(resultResponse, jobID);

        return resultResponse;
    }

    public Collection<Point> myWork(String nodeID, String jobID){
        Set<Point> myResult = new HashSet<>();
        if(nodeID.equals(database.getInfo().getId())){
            // TODO: Zasto ovo
            if(database.getInfo().getMyRegion() == null)
                return new ArrayList<>();

            if(database.getInfo().getMyRegion().getJob().getId().equals(jobID))
                myResult.addAll(database.getData());


        }else{
            List<String> recepient = new ArrayList<>();
            recepient.add(nodeID);

            ResultResponse resultResponse = messageService.sendGetResult(jobID, recepient);

            myResult.addAll(resultResponse.getData());
        }

        for(BackupInfo backupInfo : database.getBackups().values()){
            if(backupInfo.getJobID().equals(jobID)){
                myResult.addAll(backupInfo.getData());                                  // Dodajemo bakcup za taj posao ako ga imamo
            }
        }

        System.out.println(database.getInfo().getId() + " vraca " + myResult);
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

        File file = new File("result.png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
