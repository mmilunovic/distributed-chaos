package rs.raf.javaproject.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rs.raf.javaproject.repository.Database;

@Component
public class HealthChecker {

    @Autowired
    Database database;

    @Scheduled(fixedDelay = 1000)
    public void check(){
        System.out.println("test");
    }
}
