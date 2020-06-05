package org.example.repository;

import lombok.Data;
import org.example.config.ServentConfig;
import org.example.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

@Component
@Data
public class Database {

    @Autowired
    ServentConfig config;

    private final TreeSet<Node> allNodes;
    private final TreeSet<Job> allJobs;
    // node - jobID:regionID
    private final HashMap<Node, String> currentWork;

    private final ArrayList<Point> myData;
    private Region myRegion;

    // backupID - backup
    private final Map<String, Backup> backups;
    private final Map<String, Region> regions;


    public Node getServent() {
        return config.getServent();
    }
}
