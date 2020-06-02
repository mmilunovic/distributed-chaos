package rs.raf.javaproject.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.model.Region;
import rs.raf.javaproject.repository.Database;

import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

@Data
@AllArgsConstructor
public class JobExecution implements Runnable {

    private Database database;
    private Region region;
    private AtomicBoolean pause;

    @Override
    public void run() {

        //database.getData().add((database.getTracepoint() - region.getStartingPoints().get(0))*region.getJob().getProportion());
        /*region.getJob()
        database.getData()
        database.getTracepoint()*/
    }

    private Point randomPoint(){
        Random r = new Random();
        // TODO: Opseg random tacke, da li je width i height ili kako?
        double x = r.nextDouble();
        double y = r.nextDouble();

        return new Point(x, y);
    }
}
