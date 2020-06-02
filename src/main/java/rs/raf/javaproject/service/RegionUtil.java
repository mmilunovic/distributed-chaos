package rs.raf.javaproject.service;

import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Region;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RegionUtil {


    public static java.util.List<String> getAllSubregionNodeIDs(Region region) {
        if (region.getNode() != null)
            return Collections.singletonList(region.getNode().getId());

        List<String> result = new ArrayList<>();
        for (Region child : region.getChildren().values())
            result.addAll(getAllSubregionNodeIDs(child));

        return result;
    }
    public static Region getRegionFromID(Job job, String id) {
        if (id.length() == 0)
            return job.getRegions().get("");
        return getRegionFromID(job.getRegions().get(id.charAt(0)),(id.substring(1)));
    }

    public static Region getRegionFromID(Region region, String id) {
        if (id.length() == 0)
            return region;

        if (region.getChildren().size() == 0) {
            //TODO: ovde treba biti pazljiv oko fullID, ovde ce vratiti parent podregiona koji se trenutno ne obradjuje
            return region;
        }

        return getRegionFromID(region.getChildren().get(id.charAt(0)),(id.substring(1)));
    }
}
