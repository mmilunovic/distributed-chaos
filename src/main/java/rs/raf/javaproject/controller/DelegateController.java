package rs.raf.javaproject.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.service.JobService;

import java.util.Collection;

@RestController
@RequestMapping("api/delegate/{nodeID}")
    public class DelegateController {

        @Autowired
        JobService jobService;

    @GetMapping("jobs/{jobID}")
    @ResponseBody
    public Collection<Point> myWork(@PathVariable String nodeID, @PathVariable String jobID){
        return jobService.myWork(nodeID, jobID);
    }
}
