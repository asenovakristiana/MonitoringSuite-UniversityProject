 package com.monitoring.service;
 
 import com.monitoring.model.Metrics;
 
 
 
 
 
 
 
 
 
 
 
 public class NginxStatusParser
 {
   public void fillFromStatus(Metrics m, String body) {
     if (body == null || body.isEmpty())
       return; 
     String[] lines = body.split("\n");
     for (String line : lines) {
       line = line.trim();
       if (line.startsWith("Active connections:")) {
         String v = line.substring("Active connections:".length()).trim();
         m.setActiveConnections(parseInt(v));
       } 
     } 
   }
   private int parseInt(String s) {
     
     try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
   
   }
 }


