package org.example.controller;

import org.example.model.Node;
import org.example.service.DatabaseService;
import org.example.service.ReconstructionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("api/test")
public class TestController {

    @Autowired
    DatabaseService databaseService;

    @Autowired
    ReconstructionService reconstructionService;

    @GetMapping
    @ResponseBody
    public Collection<Node> getNodes(){
        return databaseService.getSuccessorTable();
    }

    @GetMapping("/delegate/{nodeID}")
    @ResponseBody
    public Node getNodes(@PathVariable String nodeID){
        System.out.println(databaseService.getSuccessorTable());
        return reconstructionService.getDelegatorFromTable(databaseService.getNodeFromID(nodeID));
    }
}
