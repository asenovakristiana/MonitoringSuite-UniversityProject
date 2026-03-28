 package com.monitoring.model;
 
 import com.monitoring.docker.DockerService;
 import java.io.Serializable;
 
 
 
 
 
 
 
 
 
 
 
 public class ServerInfo
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private String name;
   private String type;
   private String containerId;
   private String host;
   private int port;
   
   public String getName() {
     return this.name; } public void setName(String name) {
     this.name = name;
   }
   public String getType() { return this.type; } public void setType(String type) {
     this.type = type;
   }
   public String getContainerId() { return this.containerId; } public void setContainerId(String containerId) {
     this.containerId = containerId;
   }
   public String getHost() { return this.host; } public void setHost(String host) {
     this.host = host;
   }
   public int getPort() { return this.port; } public void setPort(int port) {
     this.port = port;
   }
   public String fetchStatus() throws Exception {
     if ("apache".equalsIgnoreCase(this.type)) {
       return DockerService.execInContainer(this.containerId, "curl -s http://localhost/server-status?auto");
     }
 
     
     if ("nginx".equalsIgnoreCase(this.type)) {
       return DockerService.execInContainer(this.containerId, "curl -s http://localhost/nginx_status");
     }
 
 
     
     return "";
   }
 }


