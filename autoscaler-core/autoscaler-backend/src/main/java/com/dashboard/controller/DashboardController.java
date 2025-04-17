package com.dashboard.controller;

import com.dashboard.service.DockerScalerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class DashboardController {

    @Autowired
    private DockerScalerService dockerScalerService;

    @GetMapping("/cpu-usage")
    public double getCpuUsage() {
        return dockerScalerService.fetchCpuUsage();
    }

    @GetMapping("/replicas")
    public int getReplicas() {
        return dockerScalerService.getCurrentReplicas();
    }

    @PostMapping("/evaluate-scaling")
    public void triggerAutoScaling() {
        dockerScalerService.evaluateAutoScaling();
    }
}
