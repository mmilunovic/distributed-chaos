package rs.raf.javaproject.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class HealthChecker {

    @Scheduled(fixedDelay = 1000)
    public void check(){
        System.out.println("test");
    }
}
