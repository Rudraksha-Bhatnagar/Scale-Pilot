package com.dashboard.controller;

import com.dashboard.service.DockerScalerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") // Allow access from frontend
public class DashboardController {

    private final DockerScalerService scalerService;

    public DashboardController(DockerScalerService scalerService) {
        this.scalerService = scalerService;
    }

    @GetMapping("/cpu-usage")
    public double getCpuUsage() {
        return scalerService.fetchCpuUsage();
    }

    @PostMapping("/manual-scale")
    public String manualScale(@RequestParam int replicas) {
        scalerService.scaleManually(replicas);
        return "Scaling triggered to " + replicas + " replicas.";
    }

    @GetMapping("/manual-replicas")
    public int getManualReplicas() {
        return scalerService.getCurrentReplicas();
    }
}
