 package com.monitoring.model;
 
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 public class Metrics
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private long timestamp;
   private String serverName;
   private String type;
   private int activeConnections;
   private int requests;
   private int responseTimeMs;
   private double cpu;
   private double ram;
   private double diskRead;
   private double diskWrite;
   private double netIn;
   private double netOut;
   private int errors4xx;
   private int errors5xx;
   private int busyWorkers;
   private int idleWorkers;
   private double reqPerSec;
   private double bytesPerSec;
   
   public Metrics(long timestamp, String serverName, String type, int activeConnections, int requests, int responseTimeMs, double cpu, double ram, double diskRead, double diskWrite, double netIn, double netOut, int errors4xx, int errors5xx, int busyWorkers, int idleWorkers, double reqPerSec, double bytesPerSec) {
     this.timestamp = timestamp;
     this.serverName = serverName;
     this.type = type;
     this.activeConnections = activeConnections;
     this.requests = requests;
     this.responseTimeMs = responseTimeMs;
     this.cpu = cpu;
     this.ram = ram;
     this.diskRead = diskRead;
     this.diskWrite = diskWrite;
     this.netIn = netIn;
     this.netOut = netOut;
     this.errors4xx = errors4xx;
     this.errors5xx = errors5xx;
     this.busyWorkers = busyWorkers;
     this.idleWorkers = idleWorkers;
     this.reqPerSec = reqPerSec;
     this.bytesPerSec = bytesPerSec;
   }
   public long getTimestamp() { return this.timestamp; }
   public String getServerName() { return this.serverName; } public String getType() {
     return this.type;
   }
   public int getActiveConnections() { return this.activeConnections; }
   public int getRequests() { return this.requests; } public int getResponseTimeMs() {
     return this.responseTimeMs;
   }
   public double getCpu() { return this.cpu; } public double getRam() {
     return this.ram;
   }
   public double getDiskRead() { return this.diskRead; } public double getDiskWrite() {
     return this.diskWrite;
   }
   public double getNetIn() { return this.netIn; } public double getNetOut() {
     return this.netOut;
   }
   public int getErrors4xx() { return this.errors4xx; } public int getErrors5xx() {
     return this.errors5xx;
   }
   public int getBusyWorkers() { return this.busyWorkers; } public int getIdleWorkers() {
     return this.idleWorkers;
   }
   public double getReqPerSec() { return this.reqPerSec; } public double getBytesPerSec() {
     return this.bytesPerSec;
   }
   public void setActiveConnections(int activeConnections) { this.activeConnections = activeConnections; }
   public void setRequests(int requests) { this.requests = requests; } public void setResponseTimeMs(int responseTimeMs) {
     this.responseTimeMs = responseTimeMs;
   }
   public void setCpu(double cpu) { this.cpu = cpu; } public void setRam(double ram) {
     this.ram = ram;
   }
   public void setDiskRead(double diskRead) { this.diskRead = diskRead; } public void setDiskWrite(double diskWrite) {
     this.diskWrite = diskWrite;
   }
   public void setNetIn(double netIn) { this.netIn = netIn; } public void setNetOut(double netOut) {
     this.netOut = netOut;
   }
   public void setErrors4xx(int errors4xx) { this.errors4xx = errors4xx; } public void setErrors5xx(int errors5xx) {
     this.errors5xx = errors5xx;
   }
   public void setBusyWorkers(int busyWorkers) { this.busyWorkers = busyWorkers; } public void setIdleWorkers(int idleWorkers) {
     this.idleWorkers = idleWorkers;
   }
   public void setReqPerSec(double reqPerSec) { this.reqPerSec = reqPerSec; } public void setBytesPerSec(double bytesPerSec) {
     this.bytesPerSec = bytesPerSec;
   }
 }


