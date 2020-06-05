package org.example.service;

import org.example.model.*;
import org.example.request.bootstrap.NodeLeftRequest;
import org.example.request.job.SingleResult;
import org.example.request.job.StartJobRequest;
import org.example.request.node.*;
import org.example.request.bootstrap.HailRequest;
import org.example.request.bootstrap.NewNodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.HashSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessageService {

    @Autowired
    UrlFactory urlFactory;

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ReconstructionService reconstructionService;

    private ExecutorService pool;

    @PostConstruct
    public void init(){
        pool = Executors.newFixedThreadPool(20);
    }

    private void submit(Runnable task){
        pool.submit(task);
    }

    public Node sendBootstrapHail() {
        HailRequest hailRequest = new HailRequest(urlFactory.getBootstrapHailUrl());
        return hailRequest.execute();
    }

    public void sendBootstrapNew(Node servent) {
        submit(new Runnable() {
            @Override
            public void run() {
                NewNodeRequest newNodeRequest = new NewNodeRequest(urlFactory.getBootstrapNewUrl(), servent);
                newNodeRequest.execute();
            }
        });
    }

    public void sendBootstrapLeft(Node exitingNode) {
        pool.submit(new Runnable() {
            @Override
            public void run() {
                NodeLeftRequest nodeLeftRequest = new NodeLeftRequest(urlFactory.getBoostrapNodeLeftUrl(), exitingNode);
                nodeLeftRequest.execute();
            }
        });
    }

    public Boolean sendPing(Node receiver, Node delegator,  int timeout) {
        PingRequest pingRequest = new PingRequest(urlFactory.getPingUrl(receiver, delegator), timeout);
        return pingRequest.execute();
    }

    public Collection<Node> sendGetAllNodes(Node receiver) {
        GetAllNodesRequest getAllNodesRequest = new GetAllNodesRequest(urlFactory.getGetAllNodesUrl(receiver));
        return getAllNodesRequest.execute();
    }

    public Collection<Job> sendGetAllJobs(Node receiver) {
        GetAllJobsRequest getAllJobsRequest = new GetAllJobsRequest(urlFactory.getGetAllJobsUrl(receiver));
        return getAllJobsRequest.execute();
    }

    public void sendUpdateNewNode(Node receiver, Node servent) {
        submit(new Runnable() {
            @Override
            public void run() {
                UpdateNewNodeRequest updateNewNodeRequest = new UpdateNewNodeRequest(urlFactory.getSendUpdateNewNodeUrl(receiver),servent);
                updateNewNodeRequest.execute();
            }
        });

    }

    public void broadcastNewNode(Collection<Node> broadcastReceiverNodes, Node servent) {
        for(Node receiver : broadcastReceiverNodes){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    AddNewNodeRequest addNewNodeRequest = new AddNewNodeRequest(urlFactory.getBroadcastNewNodeUrl(receiver), servent);
                    addNewNodeRequest.execute();
                }
            });

        }
    }

    public void broadcastNodeLeft(Node exitingNode) {
        for(Node receiver : databaseService.getMyBroadcastingNodes()){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    BroadcastNodeLeftRequest broadcastNodeLeftRequest = new BroadcastNodeLeftRequest(urlFactory.getBroadcastNodeLeftUrl(receiver), exitingNode);
                    broadcastNodeLeftRequest.execute();
                }
            });
        }
    }

    public void broadcastStartJob(Job job, Collection<Node> myBroadcastingNodes) {
        for(Node receiver : myBroadcastingNodes){
            pool.submit(new Runnable() {
                @Override
                public void run() {
                    StartJobRequest startJobRequest = new StartJobRequest(urlFactory.getStartJobUrl(receiver), job);
                    startJobRequest.execute();
                }
            });
        }
    }

    public HashSet<Point> sendGetResult(Job requestedJob, Region requestedRegion, Collection<Node> receivers) {
        HashSet<Point> result = new HashSet<>();

        for(Node receiver : receivers){
            Node delegator = reconstructionService.getDelegatorFromTable(receiver);
            Backup receiverBackup = sendGetBackup(delegator, receiver, requestedJob.getId(), requestedRegion.getId());
            result.addAll(receiverBackup.getData());
        }
        return result;
    }

    public Backup sendGetBackup(Node delegator, Node finalDestination, String jobID, String regionID) {
        GetBackup getBackup = new GetBackup(urlFactory.getGetBackupUrl(delegator, jobID, regionID), finalDestination);
        return getBackup.execute();
    }

    public void sendSaveBackup(Backup backup, Node destination) {
        // TODO: treba proslediti backup destination
    }
}
