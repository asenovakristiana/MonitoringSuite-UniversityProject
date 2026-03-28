 package com.monitoring.service;
 
 import com.monitoring.docker.DockerStatsService;
 import com.monitoring.model.Metrics;
 import com.monitoring.model.ServerInfo;
 
 
 
 
 
 
 
 
 
 
 
 public class MetricsFetcher
 {
   private final ApacheStatusParser apacheParser = new ApacheStatusParser();
   private final NginxStatusParser nginxParser = new NginxStatusParser();
   private final DockerStatsService dockerStatsService = new DockerStatsService();
 
   
   public Metrics fetch(ServerInfo server) {
     long ts = System.currentTimeMillis();
 
 
 
     
     Metrics m = new Metrics(ts, server.getName(), server.getType(), 0, 0, 0, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0, 0, 0, 0, 0.0D, 0.0D);
 
 
 
 
 
 
 
 
 
     
     try {
       String body = server.fetchStatus();
       
       if ("apache".equalsIgnoreCase(server.getType())) {
         Metrics parsed = this.apacheParser.parse(server.getName(), server.getType(), body);
         
         m.setActiveConnections(parsed.getActiveConnections());
         m.setBusyWorkers(parsed.getBusyWorkers());
         m.setIdleWorkers(parsed.getIdleWorkers());
         m.setReqPerSec(parsed.getReqPerSec());
         m.setBytesPerSec(parsed.getBytesPerSec());
         m.setErrors4xx(parsed.getErrors4xx());
         m.setErrors5xx(parsed.getErrors5xx());
       }
       else if ("nginx".equalsIgnoreCase(server.getType())) {
         this.nginxParser.fillFromStatus(m, body);
       } 
       
       DockerStatsService.ContainerStats stats = this.dockerStatsService.getStats(server.getContainerId());
       m.setCpu(stats.cpuPercent);
       m.setRam(stats.memPercent);
       m.setNetIn(stats.netIn);
       m.setNetOut(stats.netOut);
     
     }
     catch (Exception e) {
       e.printStackTrace();
     } 
     
     return m;
   }
 }


