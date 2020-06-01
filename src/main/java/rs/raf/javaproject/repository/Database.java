package rs.raf.javaproject.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import rs.raf.javaproject.config.MyConfig;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Node;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;


@Component
public class Database {

    @Autowired
    MyConfig config;

    Collection<Node> allNodes = new ArrayList<>();
    Collection<Job> allJobs = new ArrayList<>();

    public Node getInfo() {
        return config.getMe();
    }

    public boolean remove(Node node) {
        return allNodes.remove(node);
    }

    public boolean addNode(Node node) {
        return allNodes.add(node);
    }

    public Collection<Node> getAllNodes() {
        return allNodes;
    }

    public boolean addNodes(Collection<Node> nodes) {
        System.out.println(nodes);
        return allNodes.addAll(nodes);
    }

    public boolean remove(Job job) {
        return allJobs.remove(job);
    }

    public boolean addJob(Job job) {
        return allJobs.add(job);
    }

    public boolean addJobs(Collection<Job> jobs){
        return allJobs.addAll(jobs);
    }

    public Collection<Job> getAllJobs() {
        return allJobs;
    }

    public Job getJobByJobID(String jobID){
        for(Job job: allJobs){
            if(job.getID().equals(jobID)){
                return job;
            }
        }
        return null;
    }

    public Node getPredecessor(){
        return null;
    }

    public Node getSuccessor(){
        return null;
    }

}
