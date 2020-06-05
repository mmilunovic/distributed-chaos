package org.example.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.Objects;

@Data
public class Region implements Comparable<Region>{
    private String id;
    @ToString.Exclude
    private ArrayList<Point> startingPoints;
    @ToString.Exclude
    private Point tracepoint;
    @ToString.Exclude
    private double proportion;

    public Region(String id){
        this.id = id;
        this.startingPoints = new ArrayList<>();
    }

    public Region(Job job){
        this.tracepoint = job.getTracepoint();
        this.proportion = job.getProportion();
        this.startingPoints = new ArrayList<>(job.getStartingPoints());
        this.id = "-";
    }

    public void addPoint(Point point){
        this.startingPoints.add(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Region region = (Region) o;
        return Objects.equals(id, region.id);
    }

    @Override
    public int compareTo(Region o) {
        Integer thisID = id.length();
        Integer otherID = o.id.length();

        if(thisID.compareTo(otherID) == 0){
            return this.id.compareTo(o.id);
        }

        return thisID.compareTo(otherID);
    }

    public static Region createSubRegionFor(int subRegion, Region region){
        Region res = new Region(region.getId()+subRegion);
        res.setTracepoint(region.getTracepoint());
        res.setProportion(region.getProportion());

        Point regionCourner = region.getStartingPoints().get(subRegion);

        for(Point point : region.getStartingPoints()){
            Point newPoint = new Point(
                    regionCourner.getX() + res.proportion * (point.getX() - regionCourner.getX()),
                    regionCourner.getY() + res.proportion * (point.getY() - regionCourner.getY()));
            System.out.println( regionCourner + " - " +point + " - " + newPoint);
            res.addPoint(newPoint);
        }


        return res;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
