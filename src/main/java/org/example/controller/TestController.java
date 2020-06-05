package org.example.controller;

import org.example.model.Node;
import org.example.service.DatabaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
@RequestMapping("api/test")
public class TestController {

    @Autowired
    DatabaseService databaseService;

    @GetMapping
    @ResponseBody
    public Collection<Node> getNodes(){
        return databaseService.getNodesForJobIDAndRegionID("job1", "-");
    }
}
