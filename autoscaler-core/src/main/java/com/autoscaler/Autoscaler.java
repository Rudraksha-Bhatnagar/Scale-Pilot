package com.autoscaler;

public class Autoscaler {
    public static void main(String[] args) {
        String serviceName = Config.getProperty("service.name");
        while (true) {
            DecisionEngine.evaluateScaling(serviceName);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
