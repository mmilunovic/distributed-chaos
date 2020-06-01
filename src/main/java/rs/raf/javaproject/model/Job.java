package rs.raf.javaproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Job {

    private String id;
    private ArrayList<Point> points = new ArrayList<>();
    private long width;
    private long height;


    public String getID() {
        return id;
    }

    public Collection<Point> getPoints() {
        return points;
    }

    public long getWidth() {
        return width;
    }

    public long getHeight() {
        return height;
    }
}
