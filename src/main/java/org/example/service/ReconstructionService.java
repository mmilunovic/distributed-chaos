package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReconstructionService {

    @Autowired
    DatabaseService databaseService;

    public synchronized void reconstruct(){
        System.out.println("Reconstructing...");
    }
}
