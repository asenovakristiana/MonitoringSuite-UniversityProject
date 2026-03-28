 package com.monitoring.service;
 
 import com.monitoring.model.Metrics;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.io.IOException;
 import java.io.OutputStream;
 import java.io.OutputStreamWriter;
 import java.io.Writer;
 import java.nio.charset.StandardCharsets;
 
 
 
 
 
 public class CsvLogger
 {
   private final File file;
   
   public CsvLogger(String projectRoot) {
     this.file = new File(projectRoot + "/metrics/metrics.csv");
     
     try {
       if (!this.file.exists()) {
         writeHeader();
       }
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   private void writeHeader() throws IOException {
     try (OutputStream os = new FileOutputStream(this.file, false)) {
       os.write(new byte[] { -17, -69, -65 });
     } 
     
     try (Writer writer = new OutputStreamWriter(new FileOutputStream(this.file, true), StandardCharsets.UTF_8)) {
 
 
       
       writer.write("timestamp,serverName,type,activeConnections,requests,responseTimeMs,cpu,ram,diskRead,diskWrite,netIn,netOut,errors4xx,errors5xx,busyWorkers,idleWorkers,reqPerSec,bytesPerSec\n");
     } 
   }
 
 
 
 
 
 
   
   public void log(Metrics m) {
     try (Writer writer = new OutputStreamWriter(new FileOutputStream(this.file, true), StandardCharsets.UTF_8)) {
 
 
       
       writer.write(m
           .getTimestamp() + "," + m
           .getServerName() + "," + m
           .getType() + "," + m
           .getActiveConnections() + "," + m
           .getRequests() + "," + m
           .getResponseTimeMs() + "," + m
           .getCpu() + "," + m
           .getRam() + "," + m
           .getDiskRead() + "," + m
           .getDiskWrite() + "," + m
           .getNetIn() + "," + m
           .getNetOut() + "," + m
           .getErrors4xx() + "," + m
           .getErrors5xx() + "," + m
           .getBusyWorkers() + "," + m
           .getIdleWorkers() + "," + m
           .getReqPerSec() + "," + m
           .getBytesPerSec() + "\n");
 
     
     }
     catch (Exception e) {
       e.printStackTrace();
     } 
   }
 }


