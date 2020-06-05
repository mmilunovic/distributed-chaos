package org.example.service;

import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageService {

    @Autowired
    UrlFactory urlFactory;


    public Node sendBootstrapHail() {
        return null; // TODO
    }

    public void sendBootstrapNew() {
    }
}
