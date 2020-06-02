package rs.raf.javaproject.handler;

import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ExecutorPool {

    private ExecutorService pool;

    public ExecutorPool(){
        pool = Executors.newFixedThreadPool(20);
    }

    public void submit(Runnable task){
        pool.submit(task);
    }

}
