package rs.raf.javaproject.model;


import java.util.ArrayList;
import java.util.Collection;

public class Job {

    private String id;
    private ArrayList<Point> startingPoints = new ArrayList<>();
    private long width;
    private long height;
    private Double proportion;


    public String getID() {
        return id;
    }

    public Collection<Point> getStartingPoints() {
        return startingPoints;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }

    public Double getProportion() { return proportion; }
}
