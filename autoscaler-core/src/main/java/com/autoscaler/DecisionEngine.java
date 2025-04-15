package com.autoscaler;

import java.util.logging.Logger;

public class DecisionEngine {
    private static final Logger LOGGER = Logger.getLogger(DecisionEngine.class.getName());
    private static final double THRESHOLD_CPU_UP = Config.getDouble("cpu.threshold.up", 75.0);
    private static final double THRESHOLD_CPU_DOWN = Config.getDouble("cpu.threshold.down", 30.0);
    private static final int MAX_REPLICAS = Config.getInt("max.replicas", 10);
    private static final int MIN_REPLICAS = Config.getInt("min.replicas", 1);
    public static boolean autoScalingEnabled = true;

    public static void evaluateScaling(String serviceName) {
        if (!autoScalingEnabled) return;

        double cpuUsage = MetricsFetcher.getCpuUsage();
        int currentReplicas = DockerServiceManager.getReplicas(serviceName);

        if (cpuUsage > THRESHOLD_CPU_UP && currentReplicas < MAX_REPLICAS) {
            DockerServiceManager.scaleService(serviceName, currentReplicas + 1);
        } else if (cpuUsage < THRESHOLD_CPU_DOWN && currentReplicas > MIN_REPLICAS) {
            DockerServiceManager.scaleService(serviceName, currentReplicas - 1);
        }

        LOGGER.info("CPU Usage: " + cpuUsage + "%, Current Replicas: " + currentReplicas);
    }
}
