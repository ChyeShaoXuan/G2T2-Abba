package com.g4t2project.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class G4t2projectController {
    @RequestMapping("/Testing")
    public String Testing(){
        return "This is a test";
    }
}
