package rs.raf.javaproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.INode;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.service.NodeService;

import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/node")
public class NodeController {

    @Autowired
    private NodeService service;


    @GetMapping("/info")
    @ResponseBody
    public INode info(@RequestBody Node node){
        return service.info(node);
    }

    @GetMapping("/allNodes")
    @ResponseBody
    public Collection<INode> allNodes(){
        return service.allNodes();
    }

    @PostMapping("/ping/{nodeID}")
    @ResponseBody
    public Boolean ping(@PathVariable String nodeID){
        return service.ping(nodeID);
    }

    @PostMapping("/quit")
    public void quit(){
        service.quit();
    }

    @PostMapping("/left/{nodeID}")
    public void left(@PathVariable String nodeID){
        service.left(nodeID);
    }

    @PostMapping("/new/{nodeID}")
    public void newNode(@PathVariable String nodeID){
        service.newNode(nodeID);
    }
}
