package rs.raf.javaproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.service.JobService;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/jobs")
public class JobController {

    @Autowired
    private JobService service;
}
