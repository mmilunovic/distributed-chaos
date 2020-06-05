package org.example.controller;

import org.example.model.Job;
import org.example.model.Node;
import org.example.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @GetMapping("/info")
    @ResponseBody
    public Node getInfo(){
        return nodeService.getInfo();
    }

    @GetMapping("/allNodes")
    @ResponseBody
    public Collection<Node> allNodes(){
        return nodeService.allNodes();
    }

    @GetMapping("/allJobs")
    @ResponseBody
    public Collection<Job> getAllJobs(){
        return nodeService.getAllJobs();
    }
}
