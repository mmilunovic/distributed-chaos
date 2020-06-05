package org.example.controller;

import org.example.model.Backup;
import org.example.model.Node;
import org.example.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    NodeService nodeService;

    @GetMapping("/info")
    @ResponseBody
    public Node getInfo(){ return nodeService.getInfo(); }

    @GetMapping("/allNodes")
    @ResponseBody
    public Collection<Node> allNodes(){
        return nodeService.allNodes();
    }

    @GetMapping("/ping/{nodeID}")
    public Boolean ping(@PathVariable String nodeID){
        return nodeService.ping(nodeID);
    }

    @PutMapping("/updateNewNode")
    public void updateNewNode(@RequestBody Node newNode){
        nodeService.updateNewNode(newNode);
    }

    @PutMapping("/broadcastNewNode")
    public void broadcastNewNode(@RequestBody Node newNode){
        nodeService.broadcastNewNode(newNode);
    }

    @PostMapping("/left")
    public void left(@RequestBody Node exitingNode){
        nodeService.nodeLeft(exitingNode);
    }

    @GetMapping("/quit")
    public void quit(){ nodeService.quit(); }

    @PostMapping("/backup")
    public void saveBackup(@RequestBody Backup backup){
        nodeService.saveBackup(backup);
    }
    //TODO: Backupi, ili ih staviti na drugi kontroler?


}
