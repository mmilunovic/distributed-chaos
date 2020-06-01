package rs.raf.javaproject.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;

public class Job implements IJob{

    private String id;
    @Autowired
    private ArrayList<Point> points;
    private long width;
    private long height;


    @Override
    public String getID() {
        return id;
    }

    @Override
    public Collection<Point> getPoints() {
        return points;
    }

    @Override
    public long getWidth() {
        return width;
    }

    @Override
    public long getHeight() {
        return height;
    }
}
