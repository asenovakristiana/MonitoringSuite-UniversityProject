package com.monitoring.docker;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class DockerStatsService {

    private final String windowsApi = "http://localhost:2375";
    private final String linuxApi = "http://localhost:2375";
    private final String linuxSocket = "unix:///var/run/docker.sock";

    private String detectDockerApi() {
        if (isReachable(windowsApi + "/_ping")) {
            return windowsApi;
        }
        if (isReachable(linuxApi + "/_ping")) {
            return linuxApi;
        }
        return linuxSocket;
    }

    private boolean isReachable(String url) {
        try {
            HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
            con.setConnectTimeout(500);
            con.setReadTimeout(500);
            con.setRequestMethod("GET");
            int code = con.getResponseCode();
            return code == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public ContainerStats getStats(String containerId) throws Exception {

        String base = detectDockerApi();

        if (base.startsWith("unix://")) {
            throw new UnsupportedOperationException(
                "Docker Unix socket изисква отделна имплементация (OkHttp + UnixSocketFactory)"
            );
        }

        String url = base + "/containers/" + containerId + "/stats?stream=false";
        String json = httpGet(url);

        if (json == null || json.isEmpty()) {
            return emptyStats();
        }

        JSONObject o = new JSONObject(json);

        JSONObject cpuStats = o.getJSONObject("cpu_stats");
        JSONObject precpuStats = o.getJSONObject("precpu_stats");

        long cpuDelta = cpuStats.getJSONObject("cpu_usage").getLong("total_usage")
                - precpuStats.getJSONObject("cpu_usage").getLong("total_usage");

        long systemDelta = cpuStats.getLong("system_cpu_usage")
                - precpuStats.getLong("system_cpu_usage");

        int cpuCount = cpuStats.has("online_cpus")
                ? cpuStats.getInt("online_cpus")
                : cpuStats.getJSONObject("cpu_usage").getJSONArray("percpu_usage").length();

        double cpuPercent = 0.0;
        if (systemDelta > 0 && cpuDelta > 0) {
            cpuPercent = cpuDelta * 1.0 / systemDelta * cpuCount * 100.0;
        }

        JSONObject memStats = o.getJSONObject("memory_stats");
        double memUsage = memStats.optLong("usage", 0L);
        double memLimit = memStats.optLong("limit", 1L);
        double memPercent = memUsage / memLimit * 100.0;

        double rx = 0.0;
        double tx = 0.0;

        if (o.has("networks")) {
            JSONObject networks = o.getJSONObject("networks");
            for (String key : networks.keySet()) {
                JSONObject net = networks.getJSONObject(key);
                rx += net.optLong("rx_bytes", 0L);
                tx += net.optLong("tx_bytes", 0L);
            }
        }

        ContainerStats s = new ContainerStats();
        s.cpuPercent = cpuPercent;
        s.memPercent = memPercent;
        s.netIn = rx;
        s.netOut = tx;

        return s;
    }

    private String httpGet(String urlStr) throws Exception {
        URL url = new URL(urlStr);
        HttpURLConnection c = (HttpURLConnection) url.openConnection();
        c.setRequestMethod("GET");
        c.setConnectTimeout(3000);
        c.setReadTimeout(3000);

        int code = c.getResponseCode();
        if (code != 200) {
            return null;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    private ContainerStats emptyStats() {
        ContainerStats s = new ContainerStats();
        s.cpuPercent = 0.0;
        s.memPercent = 0.0;
        s.netIn = 0.0;
        s.netOut = 0.0;
        return s;
    }

    public static class ContainerStats {
        public double cpuPercent;
        public double memPercent;
        public double netIn;
        public double netOut;
    }
}
