package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Point;
import org.example.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class ResultService {

    @Autowired
    MessageService messageService;

    @Autowired
    DatabaseService databaseService;

    public void getResult(String jobID) {
        getResult(jobID, "-");
    }

    public void getResult(String jobID, String regionID) {
        HashSet<Point> resultResponse = new HashSet<>();

        Job requestedJob = databaseService.getJobFromID(jobID);

        if(requestedJob == null) throw new RuntimeException("Job with " + jobID + " doesnt exist");


        Collection<Node> receivers = databaseService.getNodesForJobIDAndRegionID(jobID, regionID);

        resultResponse = messageService.sendGetResult(receivers);

        generateResultPNG(resultResponse, requestedJob, regionID);
    }

    private void generateResultPNG(HashSet<Point> resultResponse, Job job, String regionID) {

        int width = job.getWidth();
        int height = job.getHeight();

        Region region = databaseService.getRegionInfoByRegionIdAndJob(regionID, job);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        Graphics2D g2d = bufferedImage.createGraphics();

        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, width, height);

        g2d.setColor(Color.red);
        for(Point point: resultResponse){
            g2d.drawLine((int)Math.round(point.getX()), (int)Math.round(point.getY()),
                    (int)Math.round(point.getX()), (int)Math.round(point.getY()));
        }

        g2d.setColor(Color.blue);
        for(Point point: region.getStartingPoints()){
            g2d.drawLine((int)Math.round(point.getX()), (int)Math.round(point.getY()),
                    (int)Math.round(point.getX()), (int)Math.round(point.getY()));
        }

        g2d.dispose();

        File file = new File("result"+ job.getId() + ".png");
        try {
            ImageIO.write(bufferedImage, "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
