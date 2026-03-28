 package com.monitoring.service;
 
 import com.monitoring.model.Metrics;
 import com.monitoring.model.ServerInfo;
 
 
 
 
 
 
 
 
 
 
 public class MetricsCollector
 {
   private final MetricsFetcher fetcher = new MetricsFetcher();
   
   public Metrics collect(ServerInfo server) {
     try {
       return this.fetcher.fetch(server);
     }
     catch (Exception e) {
       e.printStackTrace();
       
       return new Metrics(
           System.currentTimeMillis(), server
           .getName(), server
           .getType(), 0, 0, 0, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0, 0, 0, 0, 0.0D, 0.0D);
     } 
   }
 }


