package com.autoscaler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class DockerServiceManager {
    private static final Logger LOGGER = Logger.getLogger(DockerServiceManager.class.getName());

    public static int getReplicas(String serviceName) {
        try {
            Process process = Runtime.getRuntime().exec("docker service ls | grep " + serviceName + " | awk '{print $4}' | cut -d '/' -f1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            return (output != null) ? Integer.parseInt(output.trim()) : 1;
        } catch (IOException | NumberFormatException e) {
            LOGGER.severe("Error fetching service replicas: " + e.getMessage());
            return 1;
        }
    }

    public static void scaleService(String serviceName, int replicas) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("docker", "service", "scale", serviceName + "=" + replicas);
            processBuilder.start();
            LOGGER.info("Scaling " + serviceName + " to " + replicas + " replicas.");
        } catch (IOException e) {
            LOGGER.severe("Error scaling service: " + e.getMessage());
        }
    }
}

