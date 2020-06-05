package org.example.controller;

import org.example.service.NodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("") // TODO
public class NodeController {

    @Autowired
    NodeService nodeService;
}
