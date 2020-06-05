package org.example.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Job {

    private String id;
    private ArrayList<Point> startingPoints;
    private int width;
    private int height;
    private double proportion;

    public Job(String id, int width, int height, double proportion){
        this.id = id;
        this.width = width;
        this.height = height;
        this.proportion = proportion;
        this.startingPoints = new ArrayList<>();
    }

    public void addPoint(Point point){
        this.startingPoints.add(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Job job = (Job) o;
        return Objects.equals(id, job.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
