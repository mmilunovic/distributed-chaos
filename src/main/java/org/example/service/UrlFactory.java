package org.example.service;

import org.example.config.ServentConfig;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UrlFactory {

    @Autowired
    ServentConfig config;

    public String getBootstrapHailUrl(){
        return "http://" + config.getBootstrap() + "/api/bootstrap/hail";
    }

    public String getBootstrapNewUrl(){
        return "http://" + config.getBootstrap() + "/api/bootstrap/new";
    }

    public String getBoostrapNodeLeftUrl() {return "http://" + config.getBootstrap() + "/api/bootstrap/left"; }

    public String getPingUrl(Node receiver, Node delegator) { return "http://" + delegator.getID() + "/api/node/ping/" +  receiver.getID(); }

    public String getGetAllNodesUrl(Node receiver) { return "http://" + receiver.getID() + "/api/node/allNodes"; }

    public String getGetAllJobsUrl(Node receiver) { return "http://" + receiver.getID() + "/api/jobs/allJobs"; }

    public String getSendUpdateNewNodeUrl(Node receiver) { return "http://" + receiver.getID() + "/api/node/updateNewNode"; }

    public String getBroadcastNewNodeUrl(Node receiver) { return "http://" + receiver.getID() + "/api/node/broadcastNewNode"; }

    public String getStartJobUrl(Node receiver) { return "http://" + receiver.getID() + "/api/jobs/start"; }

    public String getBroadcastNodeLeftUrl(Node receiver) { return "http://" + receiver.getID() + "/api/node/left"; }
}
