package rs.raf.javaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
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
    @ToString.Exclude
    private Job job;
    @JsonIgnore
    @ToString.Exclude
    private List<Point> startingPoints;

}
