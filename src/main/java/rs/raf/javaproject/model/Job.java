package rs.raf.javaproject.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class Job {

    private String id;
    private List<Point> startingPoints;
    private long width;
    private long height;
    private Double proportion;
    @JsonIgnore
    @ToString.Exclude
    private Map<String, Region> regions;

    public Job() {
        this.startingPoints = new ArrayList<>();
        this.regions = new HashMap<>();
    }

}
