package rs.raf.bootstrap.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import rs.raf.bootstrap.model.Node;
import rs.raf.bootstrap.service.BootstrapService;

@RestController
@RequestMapping("api/bootstrap")
public class BootstrapController {

    @Autowired
    private BootstrapService service;

    @GetMapping("/hail")
    @ResponseBody
    public Node hail(){
        return service.getConnection();
    }

    @PutMapping("/new")
    public void newNode(@RequestBody Node node){
        service.save(node);
    }

    @PostMapping("/left")
    public void leave(@RequestBody Node node){
        service.remove(node);
    }



}
