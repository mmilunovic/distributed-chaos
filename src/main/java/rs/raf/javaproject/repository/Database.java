package rs.raf.javaproject.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Node;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;


@Component
public class Database {

    @Autowired
    MyConfig config;

    Set<Node> allNodes = new TreeSet<>();
    ArrayList<Job> allJobs = new ArrayList<>();

    public synchronized Node getInfo() {
        return config.getMe();
    }

    public synchronized boolean remove(Node node) {
        return allNodes.remove(node);
    }

    public synchronized boolean addNode(Node node) {
        return allNodes.add(node);
    }

    public synchronized Collection<Node> getAllNodes() {
        return allNodes;
    }

    public synchronized boolean addNodes(Collection<Node> nodes) {
        System.out.println(nodes);
        return allNodes.addAll(nodes);
    }

    public synchronized boolean remove(Job job) {
        return allJobs.remove(job);
    }

    public synchronized boolean addJob(Job job) {
        return allJobs.add(job);
    }

    public synchronized boolean addJobs(Collection<Job> jobs){
        return allJobs.addAll(jobs);
    }

    public synchronized Collection<Job> getAllJobs() {
        return allJobs;
    }

    public synchronized Job getJobByJobID(String jobID){
        for(Job job: allJobs){
            if(job.getID().equals(jobID)){
                return job;
            }
        }
        return null;
    }

    public synchronized Node getPredecessor(){
        if(allNodes.size() == 1){
            return null;
        }else{
            ArrayList<Node> nodes = new ArrayList<>(allNodes);
            int index = nodes.indexOf(getInfo()) - 1;
            index += nodes.size();
            index %= nodes.size();

            return nodes.get(index);
        }
    }

    public synchronized Node getSuccessor(){
        if(allNodes.size() == 1){
            return null;
        }else{
            ArrayList<Node> nodes = new ArrayList<>(allNodes);
            int index = nodes.indexOf(getInfo()) + 1;
            index += nodes.size();
            index %= nodes.size();

            return nodes.get(index);
        }
    }

}
