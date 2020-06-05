package org.example.service;

import org.example.model.Job;
import org.example.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;

@Component
public class ReconstructionService {

    @Autowired
    DatabaseService databaseService;



    public synchronized void reconstruct(){
        System.out.println("Reconstructing...");

        Collection<Job> jobs = databaseService.getAllJobs();
        Collection<Node> nodes = databaseService.getAllNodes();

        int[] niz = new int[4];

        for(int i = 0; i < niz.length; i++){
            niz[i] = nodes.size()/4;

            if(nodes.size() % 4 > i){
                niz[i]++;
            }
            System.out.print(niz[i] + " ");
        }
        System.out.println();

    }

    public synchronized Node getDelegatorFromTable(Node finalDestination){
        return null;
    }


}
