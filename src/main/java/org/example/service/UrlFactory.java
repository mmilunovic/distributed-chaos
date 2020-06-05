package org.example.service;

import org.example.config.ServentConfig;
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
}
