 package com.monitoring.service;
 
 import com.monitoring.model.Metrics;
 
 
 
 
 
 
 
 
 
 
 
 public class ApacheStatusParser
 {
   public Metrics parse(String serverName, String type, String body) {
     long ts = System.currentTimeMillis();
     
     int busy = 0;
     int idle = 0;
     double reqPerSec = 0.0D;
     double bytesPerSec = 0.0D;
     int active = 0;
     
     if (body != null) {
       String[] lines = body.split("\n");
       for (String line : lines) {
         line = line.trim();
         if (line.startsWith("BusyWorkers:")) {
           busy = parseInt(line.substring("BusyWorkers:".length()).trim());
         } else if (line.startsWith("IdleWorkers:")) {
           idle = parseInt(line.substring("IdleWorkers:".length()).trim());
         } else if (line.startsWith("ReqPerSec:")) {
           reqPerSec = parseDouble(line.substring("ReqPerSec:".length()).trim());
         } else if (line.startsWith("BytesPerSec:")) {
           bytesPerSec = parseDouble(line.substring("BytesPerSec:".length()).trim());
         } 
       } 
     } 
     
     active = busy;
     
     return new Metrics(ts, serverName, type, active, 0, 0, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0, 0, busy, idle, reqPerSec, bytesPerSec);
   }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
   
   private int parseInt(String s) {
     
     try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
   
   } private double parseDouble(String s) {
     
     try { return Double.parseDouble(s); } catch (Exception e) { return 0.0D; }
   
   }
 }


