package org.example.controller;

import org.example.service.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("") // TODO
public class StatusController {

    @Autowired
    StatusService statusService;
}
