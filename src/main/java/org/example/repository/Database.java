package org.example.repository;

import lombok.Data;
import org.example.config.ServentConfig;
import org.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

@Component
@Data
public class Database {

    @Autowired
    ServentConfig config;

    private final TreeSet<Node> allNodes;
    private final ArrayList<Job> allJobs;
    // node - jobID:regionID
    private final HashMap<Node, String> currentWork;

    private final ArrayList<Point> myData;
//    private Point tracePoint; // TODO : moved to Region
    private Region myRegion;

    private final ArrayList<Backup> backups;


    public Node getServent() {
        return config.getServent();
    }
}
