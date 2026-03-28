 package com.monitoring.service;
 
 import com.monitoring.model.Metrics;
 import com.monitoring.model.Peak;
 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStreamReader;
 import java.nio.charset.StandardCharsets;
 import java.util.ArrayList;
 import java.util.List;
 
 
 
 
 
 
 
 public class PeakDetector
 {
   private final List<Peak> peaks = new ArrayList<>();
   private final File csvFile;
   
   public PeakDetector(String projectRoot) {
     this.csvFile = new File(projectRoot + "/metrics/metrics.csv");
     loadPeaksFromCsv();
   }
   
   public void check(Metrics m) {
     if (m.getCpu() > 0.3D) addPeak(m, "CPU", m.getCpu(), "CPU > 0.30"); 
     if (m.getRam() > 0.3D) addPeak(m, "RAM", m.getRam(), "RAM > 0.30"); 
     if (m.getReqPerSec() > 1.0E-6D) addPeak(m, "Req/s", m.getReqPerSec(), "Req/s > 0.000001"); 
     if (m.getErrors5xx() > 0) addPeak(m, "5xx", m.getErrors5xx(), "5xx errors"); 
   }
   
   private void addPeak(Metrics m, String metric, double value, String desc) {
     Peak p = new Peak();
     p.setTimestamp(System.currentTimeMillis());
     p.setServerName(m.getServerName());
     p.setMetric(metric);
     p.setValue(value);
     p.setDescription(desc);
     
     this.peaks.add(0, p);
     
     if (this.peaks.size() > 200)
       this.peaks.remove(this.peaks.size() - 1); 
   }
   
   public List<Peak> getPeaks() {
     return this.peaks;
   }
   
   private void loadPeaksFromCsv() {
     if (!this.csvFile.exists())
       return; 
     try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(this.csvFile), StandardCharsets.UTF_8))) {
 
 
       
       boolean skipHeader = true;
       String line;
       while ((line = br.readLine()) != null)
       {
         if (skipHeader) { skipHeader = false; continue; }
         
         String[] parts = line.split(",");
         if (parts.length < 18)
           continue; 
         Metrics m = parseMetrics(parts);
         
         check(m);
       }
     
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   private Metrics parseMetrics(String[] p) {
     return new Metrics(
         Long.parseLong(p[0]), p[1], p[2], 
 
         
         Integer.parseInt(p[3]), 
         Integer.parseInt(p[4]), 
         Integer.parseInt(p[5]), 
         Double.parseDouble(p[6]), 
         Double.parseDouble(p[7]), 
         Double.parseDouble(p[8]), 
         Double.parseDouble(p[9]), 
         Double.parseDouble(p[10]), 
         Double.parseDouble(p[11]), 
         Integer.parseInt(p[12]), 
         Integer.parseInt(p[13]), 
         Integer.parseInt(p[14]), 
         Integer.parseInt(p[15]), 
         Double.parseDouble(p[16]), 
         Double.parseDouble(p[17]));
   }
 }


