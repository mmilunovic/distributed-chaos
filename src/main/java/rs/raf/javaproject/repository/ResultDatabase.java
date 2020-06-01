package rs.raf.javaproject.repository;

import org.springframework.stereotype.Component;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.model.Result;

import java.util.HashMap;
import java.util.Map;

@Component
public class ResultDatabase {

    // key: jobID-regionID, result
    Map<String, Result> storage = new HashMap<>();

    public void savePoint(String key, Point point){

    }


}
