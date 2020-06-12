package org.example.service;

import com.sun.source.tree.Tree;
import org.example.model.Job;
import org.example.model.Node;
import org.example.model.Point;
import org.example.model.Region;
import org.example.reacurringTasks.GameOfChaos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ReconstructionService {

    @Autowired
    DatabaseService databaseService;


    GameOfChaos gameOfChaos;

    public synchronized void reconstruct(){
        System.out.println(databaseService.getInfo().getID() + " is reconstructing...");

        if(gameOfChaos == null){
            gameOfChaos = new GameOfChaos(this.databaseService, new AtomicBoolean(false));
            Thread thread = new Thread(gameOfChaos);
            thread.start();
        }

        gameOfChaos.getPause().set(true);

        databaseService.clearWork();

        ArrayList<Job> jobs = new ArrayList<>(databaseService.getAllJobs());
        ArrayList<Node> nodes = new ArrayList<>(databaseService.getAllNodes());

        databaseService.clearRegions();
        int[] niz = new int[jobs.size()];

        for(int i = 0; i < niz.length; i++){
            niz[i] = nodes.size()/jobs.size();

            if(nodes.size() % jobs.size() > i){
                niz[i]++;
            }
        }

        int sum = 0;
        for(int i = 0; i < niz.length; i++){

            Job job = jobs.get(i);

            int n = job.getStartingPoints().size();

            int freeNodes = niz[i];

            if(freeNodes >= 1){

                TreeMap<Region, Node> map = new TreeMap<>();
                Queue<Region> regionQueue = new LinkedList<>();

                int pos = sum;
                Node node = nodes.get(pos++);
                Region r = new Region(job);
                databaseService.saveRegion(job.getId()+":"+r.getId(), r);
                regionQueue.add(r);
                map.put(r, node);

                freeNodes -= 1;

                while(freeNodes >= n-1){
                    Region region = regionQueue.poll();
                    Node reconstructed = map.get(region);
                    map.remove(region);
                    Region region0 = Region.createSubRegionFor(0, region);
                    databaseService.saveRegion(job.getId()+":"+region0.getId(), region0);
                    regionQueue.add(region0);

                    map.put(region0, reconstructed);
                    for(int j = 1; j < n; j++){
                        Node newNode = nodes.get(pos++);
                        Region region1 = Region.createSubRegionFor(j, region);
                        databaseService.saveRegion(job.getId()+":"+region1.getId(), region1);
                        map.put(region1, newNode);
                        regionQueue.add(region1);
                    }

                    freeNodes -= n-1;
                }
                while(freeNodes != 0){
                    Node nodeX = nodes.get(pos++);
                    map.put(new Region(""), nodeX);
                    freeNodes--;
                }
                for(Map.Entry<Region, Node> entry: map.entrySet()){
                    databaseService.insertWork(entry.getValue(), entry.getKey(), job);
                }
            }

            sum+= niz[i];
        }

        gameOfChaos.getPause().set(false);

    }

    public synchronized Node getDelegatorFromTable(Node finalDestination){

        if(finalDestination.equals(databaseService.getInfo())){
            return databaseService.getInfo();
        }

        TreeSet<Node> set = new TreeSet<>();
        set.addAll(databaseService.getSuccessorTable());

        if(set.contains(finalDestination)){
            return finalDestination;
        }

        set.add(finalDestination);

        Node prev = null;
        int i = 0;
        for(Node tableNode: set){
            if(tableNode.equals(finalDestination) && i == 0){
                return set.last();
            }

            if(tableNode.equals(finalDestination)){
                return prev;
            }
            prev  = tableNode;
            i++;
        }

        return null;
    }


}
