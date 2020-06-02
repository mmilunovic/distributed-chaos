package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    private String id;
    private String fullID;
    private Node node;
    private Map<String, Region> children;
    @JsonIgnore
    private Job job;
    @JsonIgnore
    private ArrayList<Point> startingPoints = new ArrayList<>();

}
