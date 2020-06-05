package org.example.controller;

import org.example.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/result")
public class ResultController {

    @Autowired
    ResultService resultService;

    @GetMapping("/{jobID}")
    @ResponseBody
    public void result(@PathVariable String jobID){
        resultService.getResult(jobID);
    }

    @GetMapping("/{jobID}/{regionID}")
    @ResponseBody
    public void result(@PathVariable String jobID, @PathVariable String regionID){
        resultService.getResult(jobID, regionID);
    }
}
