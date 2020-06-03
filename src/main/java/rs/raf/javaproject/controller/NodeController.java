package rs.raf.javaproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.model.Job;
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
    public Node info(@RequestBody Node node){
        return service.info(node);
    }

    @GetMapping("/allNodes")
    @ResponseBody
    public Collection<Node> allNodes(){
        return service.allNodes();
    }

    @GetMapping("/ping/{nodeID}")
    @ResponseBody
    public Boolean ping(@PathVariable String nodeID){
        return service.ping(nodeID);
    }

    @PostMapping("/quit")
    public void quit(){
        service.quit();
    }

    @GetMapping("/left/{nodeID}")
    public void left(@PathVariable String nodeID){
        service.left(nodeID);
    }

    @GetMapping("/new/{nodeID}")
    public void newNode(@PathVariable String nodeID){
        service.newNode(nodeID);
    }

    @PostMapping("/backup")
    public void saveBackup(@RequestBody BackupInfo backupInfo){
        service.saveBackup(backupInfo);
    }

    @GetMapping("/backup/{jobID}/{regionID}")
    @ResponseBody
    public BackupInfo getBackup(@PathVariable String jobID, @PathVariable String regionID){
        return service.getBackup(jobID, regionID);
    }

    @GetMapping("/allJobs")
    @ResponseBody
    public Collection<Job> getAllJobs(){
        return service.getAllJobs();
    }
}
