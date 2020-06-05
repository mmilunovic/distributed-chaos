package org.example.service;

import org.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.example.repository.Database;

import java.util.*;

@Component
public class DatabaseService {

    @Autowired
    Database database;

    public synchronized Collection<Node> getAllNodes(){
        return new ArrayList<>(database.getAllNodes());
    }

    public synchronized Collection<Job> getAllJobs(){
        return new ArrayList<>(database.getAllJobs());
    }

    public synchronized boolean isKnown(Node node){
        return database.getAllNodes().contains(node);
    }

    // Vraca node za odredjeni id ili null ukoliko ne postoji
    public synchronized Node getNodeFromID(String nodeID){
        for(Node node: database.getAllNodes()){
            if(node.getID().equals(nodeID)){
                return node;
            }
        }
        return null;
    }

    // Vraca job za odredjeni id ili null ukoliko ne postoji
    public synchronized Job getJobFromID(String jobID){

        for(Job job: database.getAllJobs()){
            if(job.getId().equals(jobID)){
                return job;
            }
        }
        return null;
    }

    public synchronized Collection<Node> getNodesForJob(Job job){
        ArrayList<Node> nodes = new ArrayList<>();

        for(Map.Entry<Node, String> entry: database.getCurrentWork().entrySet()){
            String[] work = entry.getValue().split(":");
            if (work.length == 1){
                continue; // Skiping idle nodes
            }
            String jobID = work[0];
            String regionID = work[1];

            if(jobID.equals(job.getId())){
                nodes.add(entry.getKey());
            }
        }

        return nodes;
    }

    public synchronized Collection<Node> getNodesForJobID(String jobID){
        return getNodesForJob(getJobFromID(jobID));
    }

    public synchronized  Collection<Node> getNodesForJobAndRegion(Job job, Region region){
        ArrayList<Node> nodes = new ArrayList<>();

        for(Map.Entry<Node, String> entry: database.getCurrentWork().entrySet()){
            String[] work = entry.getValue().split(":");
            String jobID = work[0];
            String regionID = work[1];

            if(jobID.equals(job.getId()) && regionID.startsWith(region.getId())){
                nodes.add(entry.getKey());
            }
        }

        return nodes;
    }

    public synchronized Collection<Node> getNodesForJobIDAndRegionID(String jobID, String regionID){
        return getNodesForJobAndRegion(getJobFromID(jobID), new Region(regionID));
    }

    public synchronized Node getSuccessor(){
        return getSuccessorForNode(database.getServent());
    }

    public synchronized Node getSuccessorForNode(Node node){
        return  getOffsetNodeFromNode(node, 1);
    }

    public synchronized Node getPredecessor(){
        return getPredecessorForNode(database.getServent());
    }

    public synchronized Node getPredecessorForNode(Node node){
        return  getOffsetNodeFromNode(node, -1);
    }

    private Node getOffsetNodeFromNode(Node node, int offset){
        ArrayList<Node> allNodes = new ArrayList<>(getAllNodes());
        int position = allNodes.indexOf(node);
        return allNodes.get((position + offset + allNodes.size()) % allNodes.size());
    }

    public synchronized Collection<Node> getBackupNodesForNode(Node node){
        ArrayList<Node> backups = new ArrayList<>();
        backups.add(getSuccessorForNode(node));
        backups.add(getPredecessorForNode(node));
        return backups;
    }

    public synchronized Collection<Node> getBackupNodesForNodeID(String nodeID){
        return getBackupNodesForNode(getNodeFromID(nodeID)); 
    }

    public synchronized Node getInfo(){
        return database.getServent();
    }

    public synchronized void saveData(Point point){
        System.out.println(point + " is saved ");
        // TODO: Dodajemo tacku ako pripada regionu, ali uvek setujemo tracepoint
        database.getMyData().add(point);
        database.getMyRegion().setTracepoint(point);
    }

    public synchronized void saveDataCollection(Collection<Point> points){
        for(Point point: points){
            saveData(point);
        }
    }

    public synchronized List<Point> getCurrentData(){
        return database.getMyData();
    }

    public synchronized Region getMyRegion(){
        return database.getMyRegion();
    }

    public synchronized void saveNode(Node node){
        database.getAllNodes().add(node);
    }

    public synchronized void saveNodes(Collection<Node> nodes){
        database.getAllNodes().addAll(nodes);
    }

    public synchronized void saveJob(Job job){
        database.getAllJobs().add(job);
    }

    public synchronized void saveJobs(Collection<Job> jobs){
        System.out.println(jobs);
        database.getAllJobs().addAll(jobs);
    }

    public synchronized Collection<Node> getMyBroadcastingNodes(){
        ArrayList<Node> broadcastingNodes = new ArrayList<>();
        broadcastingNodes.add(getOffsetNodeFromNode(getInfo(), 1));
        broadcastingNodes.add(getOffsetNodeFromNode(getInfo(), 2));
        broadcastingNodes.add(getOffsetNodeFromNode(getInfo(), 4));

        return broadcastingNodes;
    }

    public synchronized  void removeNode(Node node){
        database.getAllNodes().remove(node);
    }

    public synchronized void insertWork(Node node, Region region, Job job){
        if(node.equals(getInfo())){
            database.setMyRegion(region);
        }
        database.getCurrentWork().put(node, job.getId()+":"+region.getId());
    }

    public synchronized void saveBackup(Backup backup){
        database.getBackups().put(backup.getID(), backup);
    }

    public Backup getBackupForBackupID(String backupID) {
        // TODO
        return null;
    }
}
