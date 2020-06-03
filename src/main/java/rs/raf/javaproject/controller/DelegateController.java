package rs.raf.javaproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.response.RegionStatusResponse;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.service.JobService;

import java.util.Collection;

@RestController
@RequestMapping("api/delegate/{nodeID}")
    public class DelegateController {

        @Autowired
        JobService jobService;

    @GetMapping("jobs/{jobID}")
    @ResponseBody
    public Collection<Point> getJobResultFromNode(@PathVariable String nodeID, @PathVariable String jobID){
        return jobService.getJobResultFromNode(nodeID, jobID);
    }

    @GetMapping("/jobs/{jobID}/{regionID}")
    @ResponseBody
    public Collection<Point> getRegionResultFromNode(@PathVariable String nodeID, @PathVariable String jobID, @PathVariable String regionID){
        return jobService.getRegionResultFromNode(nodeID, jobID, regionID);
    }

    @GetMapping("/status/{jobID}")
    @ResponseBody
    public RegionStatusResponse myStatus(@PathVariable String nodeID, @PathVariable String jobID){
        return jobService.myStatus(nodeID, jobID);
    }
}
