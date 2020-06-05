package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Point;
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
        HashSet<Point> resultResponse = new HashSet<>();

        Job requestedJob = databaseService.getJobFromID(jobID);

        Collection<Node> receivers = databaseService.getNodesForJob(requestedJob);

        generateResultPNG(resultResponse, requestedJob);
    }

    private void generateResultPNG(HashSet<Point> resultResponse, Job job) {

/*
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
        }*/
    }
}
