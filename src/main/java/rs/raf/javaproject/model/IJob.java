package rs.raf.javaproject.model;

import java.awt.*;
import java.util.Collection;

public interface IJob {

    String getID();

    Collection<Point> getPoints();

    long getWidth();

    long getHeight();
}
