package org.example.controller;

import org.example.model.Job;
import org.example.model.Node;
import org.example.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    JobService jobService;

    @GetMapping("/allJobs")
    @ResponseBody
    public Collection<Job> getAllJobs(){
        return jobService.getAllJobs();
    }

    @PutMapping("/start")
    public void start(@RequestBody Job job){
        System.out.println("Starting job: " + job.toString());
        jobService.startJob(job);
    }

    @PostMapping("/singleResult")
    public void singleResult(@RequestBody Job job){
        jobService.singleResult(job);
    }

    @GetMapping("/stopAll/{jobID}")
    public void stopAll(@PathVariable String jobID){

        //jobService.stopAll(jobID);
    }

    @DeleteMapping("/{jobID}")
    public void deleteJob(@RequestBody Node node, @PathVariable String jobID){

        //jobService.deleteJob(jobID);
    }
}
