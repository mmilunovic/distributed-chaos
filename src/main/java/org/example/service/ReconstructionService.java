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
            gameOfChaos = new GameOfChaos();
            gameOfChaos.setPause(new AtomicBoolean(false));
            Thread thread = new Thread(gameOfChaos);
            thread.start();
        }

        gameOfChaos.getPause().set(true);


        ArrayList<Job> jobs = new ArrayList<>(databaseService.getAllJobs());
        ArrayList<Node> nodes = new ArrayList<>(databaseService.getAllNodes());

//        Job job1 = new Job();
//        job1.setId("job1");
//        job1.setProportion(0.5);
//        job1.addPoint(new Point(25,25));
//        job1.addPoint(new Point(75, 25));
//        job1.addPoint(new Point(50, 75));


//        Job job2 = new Job();
//        job2.setId("job2");
//        job2.addPoint(new Point(25,25));
//        job2.addPoint(new Point(75, 25));
//        job2.addPoint(new Point(50, 75));
//
//
//        Job job3 = new Job();
//        job3.setId("job3");
//        job3.addPoint(new Point(25,25));
//        job3.addPoint(new Point(75, 25));
//        job3.addPoint(new Point(50, 75));
//
//
//        Job job4 = new Job();
//        job4.setId("job4");
//        job4.addPoint(new Point(25,25));
//        job4.addPoint(new Point(75, 25));
//        job4.addPoint(new Point(50, 75));

//        jobs.add(job1);
//        jobs.add(job2);
//        jobs.add(job3);
//        jobs.add(job4);

        int[] niz = new int[jobs.size()];

        for(int i = 0; i < niz.length; i++){
            niz[i] = nodes.size()/jobs.size();

            if(nodes.size() % jobs.size() > i){
                niz[i]++;
            }
            System.out.print(niz[i] + " ");
        }
        System.out.println();

        int sum = 0;
        for(int i = 0; i < niz.length; i++){

            Job job = jobs.get(i);

            System.out.print(job.getId() + ": ");
            int n = job.getStartingPoints().size();

            int freeNodes = niz[i];

            if(freeNodes >= 1){

                TreeMap<Region, Node> map = new TreeMap<>();
                Queue<Region> regionQueue = new LinkedList<>();

                int pos = sum;
                Node node = nodes.get(pos++);
                Region r = new Region(job);
                regionQueue.add(r);
                map.put(r, node);

                freeNodes -= 1;

                while(freeNodes >= n-1){
                    Region region = regionQueue.poll();
                    Node reconstructed = map.get(region);
                    map.remove(region);
                    Region region0 = Region.createSubRegionFor(0, region);
                    regionQueue.add(region0);

                    map.put(region0, reconstructed);
                    for(int j = 1; j < n; j++){
                        Node newNode = nodes.get(pos++);
                        Region region1 = Region.createSubRegionFor(j, region);
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
        return null;
    }


}
