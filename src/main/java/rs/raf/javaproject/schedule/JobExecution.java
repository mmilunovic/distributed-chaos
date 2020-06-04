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
    private AtomicBoolean exit;

    @Override
    public void run() {

        while(true){
            if(exit.get())
                return;
            if(pause.get()){
                sleep(JOB_SLEEP);
            }else{
                executeJob();
            }
        }
    }

    private void executeJob(){
        sleep(100);
        Point tracepoint = database.getTracepoint();
        Double proportion = region.getJob().getProportion();
        Point randomPoint = randomStartingPoint();
        Point newPoint = new Point(
                tracepoint.getX() + proportion * (randomPoint.getX() - tracepoint.getX()),
                tracepoint.getY() + proportion * (randomPoint.getY() - tracepoint.getY())
        );

//        System.out.println(database.getInfo().getId() + " je nacrtao tacku " + newPoint);
        database.getData().add(newPoint);
        database.setTracepoint(newPoint);
    }

    private Point randomPoint(){
        Random r = new Random();
        int x = r.nextInt(100);
        int y = r.nextInt(100);
        return new Point((double) x, (double) y);
    }

    private Point randomStartingPoint(){
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
