package rs.raf.javaproject.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Data
public class Job {

    private String id;
    private ArrayList<Point> startingPoints = new ArrayList<>();
    private long width;
    private long height;
    private Double proportion;
    @JsonIgnore
    private Map<String, Region> regions;

}
