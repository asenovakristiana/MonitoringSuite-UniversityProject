 package com.monitoring.docker;
 
 import com.monitoring.model.ServerInfo;
 import java.io.BufferedReader;
 import java.io.File;
 import java.io.IOException;
 import java.io.InputStreamReader;
 import java.net.ServerSocket;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.UUID;
 
 
 
 
 
 
 
 
 public class DockerService
 {
   String projectRoot = (new File(".")).getAbsolutePath();
   
   public DockerService(String projectRoot) {
     this.projectRoot = projectRoot;
   }
   
   private int findFreePort() {
     for (int port = 10000; port < 20000; port++) {
       try (ServerSocket socket = new ServerSocket(port)) {
         return port;
       } catch (Exception exception) {}
     } 
     throw new RuntimeException("No free ports available");
   }
   
   public ServerInfo createApacheServer() throws IOException, InterruptedException {
     String name = "apache-" + UUID.randomUUID().toString().substring(0, 8);
     
     File dir = new File(this.projectRoot + File.separator + "docker" + File.separator + "apache");
     
     run(new String[] { "docker", "build", "-t", name, "." }, dir);
     
     int port = findFreePort();
     
     run(new String[] { "docker", "run", "-d", "-p", port + ":80", "--name", name, name }, dir);
 
 
     
     String id = getContainerId(name);
     
     ServerInfo s = new ServerInfo();
     s.setName(name);
     s.setType("apache");
     s.setContainerId(id);
     s.setHost("localhost");
     s.setPort(port);
     
     return s;
   }
   
   public ServerInfo createNginxServer() throws IOException, InterruptedException {
     String name = "nginx-" + UUID.randomUUID().toString().substring(0, 8);
     
     File dir = new File(this.projectRoot + File.separator + "docker" + File.separator + "nginx");
     
     run(new String[] { "docker", "build", "-t", name, "." }, dir);
     
     int port = findFreePort();
     
     run(new String[] { "docker", "run", "-d", "-p", port + ":80", "--name", name, name }, dir);
 
 
     
     String id = getContainerId(name);
     
     ServerInfo s = new ServerInfo();
     s.setName(name);
     s.setType("nginx");
     s.setContainerId(id);
     s.setHost("localhost");
     s.setPort(port);
     
     return s;
   }
   
   private void run(String[] cmd, File dir) throws IOException, InterruptedException {
     ProcessBuilder pb = new ProcessBuilder(cmd);
     pb.directory(dir);
     pb.redirectErrorStream(true);
     Process p = pb.start();
     p.waitFor();
   }
   
   private String getContainerId(String name) throws IOException, InterruptedException {
     ProcessBuilder pb = new ProcessBuilder(new String[] { "docker", "inspect", "-f", "{{.Id}}", name });
     pb.redirectErrorStream(true);
     Process p = pb.start();
     
     BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
     String id = br.readLine();
     p.waitFor();
     return id;
   }
 
 
   
   public static String execInContainer(String containerId, String command) throws IOException, InterruptedException {
     ProcessBuilder pb = new ProcessBuilder(new String[] { "docker", "exec", containerId, "sh", "-c", command });
 
     
     pb.redirectErrorStream(true);
     Process p = pb.start();
 
     
     BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
 
     
     StringBuilder sb = new StringBuilder();
     
     String line;
     while ((line = br.readLine()) != null) {
       sb.append(line).append("\n");
     }
     
     p.waitFor();
     return sb.toString();
   }
 
   
   public List<ServerInfo> getRunningServers() throws IOException, InterruptedException {
     List<ServerInfo> list = new ArrayList<>();
     
     ProcessBuilder pb = new ProcessBuilder(new String[] { "docker", "ps", "--format", "{{.Names}} {{.Ports}}" });
 
     
     pb.redirectErrorStream(true);
     Process p = pb.start();
 
     
     BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
     
     String line;
     while ((line = br.readLine()) != null) {
       
       if (!line.contains("->80"))
         continue; 
       String[] parts = line.split(" ");
       if (parts.length < 2)
         continue; 
       String name = parts[0];
       String portPart = parts[1];
       
       String portStr = portPart.substring(portPart.indexOf(":") + 1, portPart.indexOf("->"));
       int port = Integer.parseInt(portStr);
       
       ServerInfo s = new ServerInfo();
       s.setName(name);
       s.setHost("localhost");
       s.setPort(port);
       
       if (name.startsWith("apache-")) {
         s.setType("apache");
       } else if (name.startsWith("nginx-")) {
         s.setType("nginx");
       } else {
         continue;
       } 
       
       s.setContainerId(getContainerId(name));
       list.add(s);
     } 
     
     p.waitFor();
     return list;
   }
 }


