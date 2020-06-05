package org.example.controller;

import org.example.model.Job;
import org.example.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
