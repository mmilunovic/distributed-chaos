package rs.raf.javaproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.IJob;
import rs.raf.javaproject.response.ResultResponse;
import rs.raf.javaproject.response.StatusResponse;
import rs.raf.javaproject.service.JobService;

import java.awt.*;
import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/{nodeID}/jobs")
public class JobController {

    @Autowired
    private JobService service;

    @GetMapping("/status")
    @ResponseBody
    public StatusResponse status(){
        return service.status();
    }

    @GetMapping("/status/{jobID}")
    @ResponseBody
    public StatusResponse status(@PathVariable String jobID){
        return service.status(jobID);
    }

    @GetMapping("/status/{jobID}/{regionID}")
    @ResponseBody
    public StatusResponse status(@PathVariable String jobID, @PathVariable String regionID){
        return service.status(jobID, regionID);
    }

    @PutMapping("/start")
    public void start(@RequestBody IJob job){
        service.start(job);
    }

    @GetMapping("/result/{jobID}")
    @ResponseBody
    public ResultResponse result(@PathVariable String jobID){
        return service.result(jobID);
    }

    @GetMapping("/result/{jobID}/{regionID}")
    @ResponseBody
    public ResultResponse result(@PathVariable String jobID, @PathVariable String regionID){
        return service.result(jobID, regionID);
    }

    @GetMapping("/{jobID}")
    @ResponseBody
    public Collection<Point> myWork(@PathVariable String nodeID, @PathVariable String jobID){
        return service.myWork(nodeID, jobID);
    }




    @PostMapping("/stopAll/{jobID}")
    public void stopAll(@PathVariable String jobID){
        service.stopAll(jobID);
    }

    @DeleteMapping("/{jobID}")
    public void deleteJob(@PathVariable String jobID){
        service.deleteJob(jobID);
    }
}
