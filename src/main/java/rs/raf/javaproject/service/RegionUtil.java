package rs.raf.javaproject.service;

import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.model.Region;

import java.util.*;

public class RegionUtil {

    public static Region getRegionFromID(Map<String, Job> jobs, String jobID, String regionID) {
        Job job = jobs.get(jobID);
        Region region = job.getRegions().get(regionID.substring(0,1));
        for (int i = 1; i < regionID.length(); i++) {
            region = region.getChildren().get(regionID.substring(i, i+1));
        }
        return region;
    }

    public static List<String> getAllJobNodeIDs(Job job) {
        List<String> result = new ArrayList<>();
        for (Region child : job.getRegions().values())
            result.addAll(getAllSubregionNodeIDs(child));

        return result;
    }

    public static List<String> getAllSubregionNodeIDs(Region region) {
        if (region.getNode() != null)
            return Collections.singletonList(region.getNode().getId());

        List<String> result = new ArrayList<>();
        for (Region child : region.getChildren().values())
            result.addAll(getAllSubregionNodeIDs(child));

        return result;
    }

    /** key - nodeID
     * value - regionID
     */
    public static Map<String, String> getAllJobNodeAndRegionIDs(Job job) {
        Map<String, String> result = new HashMap<>();
        for (Region child : job.getRegions().values())
            result.putAll(getAllSubregionNodeAndJobIDs(child));

        return result;
    }

    /** key - nodeID
     * value - regionID
     */
    public static Map<String, String> getAllSubregionNodeAndJobIDs(Region region) {
        if (region.getNode() != null)
            return Collections.singletonMap(region.getNode().getId(), region.getFullID());

        Map<String, String> result = new HashMap<>();
        for (Region child : region.getChildren().values())
            result.putAll(getAllSubregionNodeAndJobIDs(child));

        return result;
    }

    public static Region getRegionFromID(Job job, String id) {
        if (id.length() == 0)
            return job.getRegions().get("");
        return getRegionFromID(job.getRegions().get(id.substring(0,1)),(id.substring(1)));
    }

    public static Region getRegionFromID(Region region, String id) {
        if (id.length() == 0)
            return region;

        if (region.getChildren().size() == 0) {
            //TODO: ovde treba biti pazljiv oko fullID, ovde ce vratiti parent podregiona koji se trenutno ne obradjuje
            return region;
        }

        return getRegionFromID(region.getChildren().get(id.substring(0,1)),(id.substring(1)));
    }

    public static List<Point> getStartingPointsFromParent(String regionID, Double proportion, List<Point> parentPoints) {
        Integer startIndex = Integer.parseInt(regionID);
        Point startPoint = parentPoints.get(startIndex);

        List<Point> result = new ArrayList<>();
        for (int i = 0; i < parentPoints.size(); i++) {
            if (i == startIndex) {
                result.add(startPoint);
            }
            else {
                Point newPoint = new Point(
                        startPoint.getX() + proportion * (parentPoints.get(i).getX() - startPoint.getX()),
                        startPoint.getY() + proportion * (parentPoints.get(i).getY() - startPoint.getY())
                );
                result.add(newPoint);
            }
        }
        return result;

    }
}
