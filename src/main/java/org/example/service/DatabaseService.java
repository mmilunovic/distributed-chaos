package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Point;
import org.example.model.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.repository.Database;

import java.util.*;

@Component
public class DatabaseService {

    @Autowired
    Database database;

    public synchronized Collection<Node> getAllNodes(){
        return null; // TODO
    }

    public synchronized Collection<Job> getAllJobs(){
        return null; // TODO
    }

    public synchronized Node getNodeFromID(String nodeID){
        return null; // TODO
    }

    public synchronized Job getJobFromID(String jobID){
        return null; // TODO
    }

    public synchronized Collection<Node> getNodesForJob(Job job){
        return null; // TODO
    }

    public synchronized Collection<Node> getNodesForJobID(String jobID){
        return null; // TODO
    }

    public synchronized  Collection<Node> getNodesForJobAndRegion(Job job, Region region){
        return null; // TODO
    }

    public synchronized Collection<Node> getNodesForJobIDAndRegionID(String jobID, String regionID){
        return null; // TODO
    }

    public synchronized Node getSuccessor(){
        return null; // TODO
    }

    public synchronized Node getPredecessor(){
        return null; // TODO
    }

    public synchronized Collection<Node> getBackupNodesForNode(Node node){
        return null; // TODO
    }

    public synchronized Collection<Node> getBackupNodesForNodeID(String nodeID){
        return null; // TODO
    }

    public synchronized Node getInfo(){
        return database.getServent(); // TODO
    }

    public synchronized void saveData(Point point){
        // TODO
    }

    public synchronized List<Point> getCurrentData(){
        return null; // TODO
    }

    public synchronized Region getMyRegion(){
        return null; // TODO
    }

    public synchronized void saveNode(Node node){

    }


}
