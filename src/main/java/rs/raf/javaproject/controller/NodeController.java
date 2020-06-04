package rs.raf.javaproject.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.*;
import rs.raf.javaproject.model.BackupInfo;
import rs.raf.javaproject.model.Job;
import rs.raf.javaproject.model.Node;
import rs.raf.javaproject.model.Point;
import rs.raf.javaproject.service.MessageService;
import rs.raf.javaproject.service.NodeService;

import java.util.Collection;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/node")
public class NodeController implements ApplicationContextAware {

    @Autowired
    private NodeService service;

    private ApplicationContext context;

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
        return;
    }

    @Override
    public void setApplicationContext(ApplicationContext ctx) throws BeansException {
        this.context = ctx;

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
        return service.getBackup(service.getDatabase().getInfo().getId(), jobID, regionID);
    }

    @GetMapping("/allJobs")
    @ResponseBody
    public Collection<Job> getAllJobs(){
        return service.getAllJobs();
    }

    @Autowired
    MessageService messageService;

    @GetMapping("/test/{nodeID}")
    @ResponseBody
    public Collection<String> getTest(@PathVariable String nodeID){
        System.out.println("aa");
        return messageService.getBackupNodeIDForNode(nodeID);
    }
}
