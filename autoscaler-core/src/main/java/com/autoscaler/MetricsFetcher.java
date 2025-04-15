package com.autoscaler;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class MetricsFetcher {
    private static final String PROMETHEUS_URL = "http://localhost:9090/api/v1/query?query=avg(rate(container_cpu_usage_seconds_total[1m]))*100";
    private static final Logger LOGGER = Logger.getLogger(MetricsFetcher.class.getName());

    public static double getCpuUsage() {
        try {
            URL url = new URL(PROMETHEUS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                LOGGER.warning("Failed to fetch metrics from Prometheus.");
                return 0;
            }

            Scanner scanner = new Scanner(conn.getInputStream());
            StringBuilder response = new StringBuilder();
            while (scanner.hasNext()) {
                response.append(scanner.nextLine());
            }
            scanner.close();

            return parseCpuUsage(response.toString());
        } catch (IOException e) {
            LOGGER.severe("Error fetching CPU usage: " + e.getMessage());
            return 0;
        }
    }

    private static double parseCpuUsage(String jsonResponse) {
        try {
            // Look for the quoted metric value (e.g., "0.45")
            int valueIndex = jsonResponse.indexOf("value\":[");
            if (valueIndex != -1) {
                int firstQuote = jsonResponse.indexOf("\"", valueIndex);
                int secondQuote = jsonResponse.indexOf("\"", firstQuote + 1);
                if (firstQuote != -1 && secondQuote != -1) {
                    String cpuStr = jsonResponse.substring(firstQuote + 1, secondQuote);
                    return Double.parseDouble(cpuStr);
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing CPU usage: " + e.getMessage());
        }
        return 0;
    }
}

