package org.example.reacurringTasks;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.model.Point;
import org.example.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
public class GameOfChaos implements Runnable{

    private DatabaseService databaseService;

    private AtomicBoolean pause;


    @Override
    public void run() {
        while(true){
            if(pause.get())
                sleep(1000);
            else
                doChaos();
        }
    }

    private void doChaos() {
        sleep(100);

        if(databaseService.getMyRegion() != null) { // Ako nisam idle
            Point tracepoint = databaseService.getMyRegion().getTracepoint();
            Double proportion = databaseService.getMyRegion().getProportion();
            Point randomPoint = randomStartingPoint();
            if(randomPoint != null) {
                Point newPoint = new Point(
                        tracepoint.getX() + proportion * (randomPoint.getX() - tracepoint.getX()),
                        tracepoint.getY() + proportion * (randomPoint.getY() - tracepoint.getY()));

                databaseService.saveData(newPoint);
            }
        }
    }

    private Point randomStartingPoint(){
        Random r = new Random();
        Point randomStartingPoint = new Point();
        try {
             randomStartingPoint = databaseService.getMyRegion().getStartingPoints().get(r.nextInt(databaseService.getMyRegion().getStartingPoints().size()));
        } catch (Exception e){
            System.out.println(e);
        }
        return randomStartingPoint;
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
}
