package com.autoscaler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;
import java.util.logging.Logger;

public class MetricsFetcher {
    private static final String PROMETHEUS_URL =
        "http://localhost:9090/api/v1/query?query=avg(rate(container_cpu_usage_seconds_total[1m]))*100";
    private static final Logger LOGGER = Logger.getLogger(MetricsFetcher.class.getName());

    public static double getCpuUsage() {
        try {
            URL url = new URL(PROMETHEUS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            if (conn.getResponseCode() != 200) {
                LOGGER.warning("Failed to fetch metrics from Prometheus. HTTP code: " + conn.getResponseCode());
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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(jsonResponse);

            JsonNode valueNode = root
                .path("data")
                .path("result")
                .get(0)
                .path("value")
                .get(1);

            if (valueNode != null && valueNode.isTextual()) {
                return Double.parseDouble(valueNode.asText());
            } else {
                LOGGER.warning("CPU usage value not found or not a string.");
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing CPU usage: " + e.getMessage());
        }
        return 0;
    }
}
