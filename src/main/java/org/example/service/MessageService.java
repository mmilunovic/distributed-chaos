package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.example.request.bootstrap.HailRequest;
import org.example.request.bootstrap.NewNodeRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class MessageService {

    @Autowired
    UrlFactory urlFactory;

    public Node sendBootstrapHail() {
        HailRequest hailRequest = new HailRequest(urlFactory.getBootstrapHailUrl());
        return hailRequest.execute();
    }

    public void sendBootstrapNew(Node servent) {
        NewNodeRequest newNodeRequest = new NewNodeRequest(urlFactory.getBootstrapNewUrl(), servent);
        newNodeRequest.execute();
    }

    public Collection<Node> sendGetAllNodes(Node receiver) {
        return null; // TODO
    }

    public Collection<Job> sendGetAllJobs(Node receiver) {
        return null; // TODO
    }
}
