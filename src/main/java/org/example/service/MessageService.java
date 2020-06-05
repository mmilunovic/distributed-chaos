package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.request.node.*;
import org.example.request.bootstrap.HailRequest;
import org.example.request.bootstrap.NewNodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class MessageService {

    @Autowired
    UrlFactory urlFactory;

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
        NewNodeRequest newNodeRequest = new NewNodeRequest(urlFactory.getBootstrapNewUrl(), servent);
        newNodeRequest.execute();
    }

    public Boolean sendPing(Node receiver, int timeout) {
        PingRequest pingRequest = new PingRequest(urlFactory.getPingUrl(receiver), timeout);
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
        UpdateNewNodeRequest updateNewNodeRequest = new UpdateNewNodeRequest(urlFactory.getSendUpdateNewNodeUrl(receiver),servent);
        updateNewNodeRequest.execute();
    }

    public void broadcastNewNode(Collection<Node> broadcastReceiverNodes, Node servent) {
        for(Node receiver : broadcastReceiverNodes){
            BroadcastNewNodeRequest broadcastNewNodeRequest = new BroadcastNewNodeRequest(urlFactory.getBroadcastNewNodeUrl(receiver), servent);
            broadcastNewNodeRequest.execute();
        }
    }
}
