package com.dashboard.service;

import com.autoscaler.DockerServiceManager;
import com.autoscaler.MetricsFetcher;
import org.springframework.stereotype.Service;

@Service
public class DockerScalerService {

    private final String serviceName = "my_service"; // replace with your actual service name

    public double fetchCpuUsage() {
        return MetricsFetcher.getCpuUsage();
    }

    public int getCurrentReplicas() {
        return DockerServiceManager.getReplicas(serviceName);
    }

    public void evaluateAutoScaling() {
        com.autoscaler.DecisionEngine.evaluateScaling(serviceName);
    }
}
