package com.autoscaler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Logger;

public class DockerServiceManager {
    private static final Logger LOGGER = Logger.getLogger(DockerServiceManager.class.getName());

    public static int getReplicas(String serviceName) {
        try {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c",
                "docker service inspect --format='{{.Spec.Mode.Replicated.Replicas}}' " + serviceName);
            Process process = pb.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String output = reader.readLine();
            if (output != null) {
                LOGGER.info("Service replicas: " + output);
                return Integer.parseInt(output.trim());
            } else {
                LOGGER.warning("No output from service inspect.");
                return 100;
            }
        } catch (IOException | NumberFormatException e) {
            LOGGER.severe("Error fetching service replicas: " + e.getMessage());
            return 100;
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

