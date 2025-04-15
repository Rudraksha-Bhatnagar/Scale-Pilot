package com.dashboard.service;

import com.autoscaler.DockerServiceManager;
import com.autoscaler.MetricsFetcher;
import org.springframework.stereotype.Service;

@Service
public class DockerScalerService {

    private final String serviceName = "my_service"; // You can externalize this if needed

    public double fetchCpuUsage() {
        return MetricsFetcher.getCpuUsage();
    }

    public void scaleManually(int replicas) {
        DockerServiceManager.scaleService(serviceName, replicas);
    }

    public int getCurrentReplicas() {
        return DockerServiceManager.getReplicas(serviceName);
    }
}
