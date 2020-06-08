package org.example.controller;

import org.example.response.StatusResponse;
import org.example.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashSet;

@RestController
@RequestMapping("/api/status")
public class StatusController {

    @Autowired
    StatusService statusService;

    @GetMapping
    @ResponseBody
    public Collection<StatusResponse> status(){
        return statusService.getStatus();
    }

    @GetMapping("/{jobID}")
    @ResponseBody
    public Collection<StatusResponse> status(@PathVariable String jobID){
        return statusService.getStatus(jobID);
    }

    @GetMapping("/{jobID}/{regionID}")
    @ResponseBody
    public Collection<StatusResponse> status(@PathVariable String jobID, @PathVariable String regionID){
        return statusService.getStatus(jobID, regionID);
    }

}
