package org.example.service;

import org.example.model.Node;
import org.example.request.bootstrap.HailRequest;
import org.example.request.bootstrap.NewNodeRequest;
import org.example.request.node.PingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

    public Boolean sendPing(String nodeID, int i) {
        PingRequest pingRequest = new PingRequest(urlFactory.getPingUrl(nodeID), i);
        return pingRequest.execute();
    }
}
