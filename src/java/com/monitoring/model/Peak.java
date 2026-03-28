 package com.monitoring.model;
 
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 public class Peak
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private long timestamp;
   private String serverName;
   private String metric;
   private double value;
   private String description;
   
   public Peak(long timestamp, String serverName, String metric, double value, String description) {
     this.timestamp = timestamp;
     this.serverName = serverName;
     this.metric = metric;
     this.value = value;
     this.description = description;
   }
   
   public Peak() {}
   
   public long getTimestamp() { return this.timestamp; } public void setTimestamp(long timestamp) {
     this.timestamp = timestamp;
   }
   public String getServerName() { return this.serverName; } public void setServerName(String serverName) {
     this.serverName = serverName;
   }
   public String getMetric() { return this.metric; } public void setMetric(String metric) {
     this.metric = metric;
   }
   public double getValue() { return this.value; } public void setValue(double value) {
     this.value = value;
   }
   public String getDescription() { return this.description; } public void setDescription(String description) {
     this.description = description;
   }
 }


