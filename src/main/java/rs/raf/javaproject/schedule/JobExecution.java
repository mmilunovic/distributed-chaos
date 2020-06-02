package rs.raf.javaproject.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.model.Region;
import rs.raf.javaproject.repository.Database;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
public class JobExecution implements Runnable {

    private final long JOB_SLEEP = 1000;

    private Database database;
    private Region region;
    private AtomicBoolean pause;

    @Override
    public void run() {

        while(true){
            if(pause.get()){
                sleep(JOB_SLEEP);
            }else{
                executeJob();
            }
        }

    }

    private void executeJob(){
        if(database.getData().isEmpty()){
            Point newPoint = randomPoint();
            database.getData().add(newPoint);
        }

        Point tracepoint = database.getTracepoint();
        Double proportion = region.getJob().getProportion();
        Point randomPoint = randomPoint();
        Point newPoint = new Point(
                tracepoint.getX() + proportion * (randomPoint.getX() - tracepoint.getX()),
                tracepoint.getY() + proportion * (randomPoint.getY() - tracepoint.getY())
        );

        database.getData().add(newPoint);
    }

    private Point randomPoint(){
        Random r = new Random();
        return region.getStartingPoints().get(r.nextInt(region.getStartingPoints().size()));
    }

    private void sleep(long duration) {
        try {
            Thread.sleep(duration);
        } catch (InterruptedException e) {
        }
    }
}
