package org.example.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Region {
    private String id;
    private ArrayList<Point> startingPoints;
    private Point tracepoint;
    private double proportion;

    public Region(String id){
        this.id = id;
        this.startingPoints = new ArrayList<>();
    }

    public void addPoint(Point point){
        this.startingPoints.add(point);
    }

}
