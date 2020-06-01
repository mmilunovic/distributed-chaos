package rs.raf.javaproject.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.repository.Database;

import java.util.Random;

@Component
public class JobExecution {

    @Autowired
    Database database;

//    @Scheduled(fixedDelay = 500)
//    public void executeJob(){
//        String myJobID = database.getInfo().getJobID();
//        String myRegionID = database.getInfo().getRegionID();
//
//        Job myJob = database.getJobByJobID(myJobID);
//
//        Double proportion = myJob.getProportion();
//
//
//
//
//
//    }

    private Point randomPoint(){
        Random r = new Random();
        // TODO: Opseg random tacke, da li je width i height ili kako?
        double x = r.nextDouble();
        double y = r.nextDouble();

        return new Point(x, y);
    }
}
