 package com.monitoring.web;
 
 import com.monitoring.model.Metrics;
 import java.io.BufferedReader;
 import java.io.File;
 import java.io.FileInputStream;
 import java.io.InputStreamReader;
 import java.io.Serializable;
 import java.nio.charset.StandardCharsets;
 import java.util.ArrayList;
 import java.util.HashMap;
 import java.util.List;
 import java.util.Map;
 import javax.annotation.PostConstruct;
 import javax.faces.bean.ApplicationScoped;
 import javax.faces.bean.ManagedBean;
 
 
 
 
 @ManagedBean(name = "historyBean")
 @ApplicationScoped
 public class HistoryBean
   implements Serializable
 {
   private static final long serialVersionUID = 1L;
   private Map<String, List<Metrics>> fullHistory;
   
   @PostConstruct
   public void init() {
     this.fullHistory = new HashMap<>();
     loadCsvHistory();
   }
   
   private void loadCsvHistory() {
     String basePath = (new File(".")).getAbsolutePath();
     File dir = new File(basePath, "metrics");
     dir.mkdirs();
     File file = new File(dir, "metrics.csv");
     if (!file.exists())
       return; 
     long now = System.currentTimeMillis();
     long cutoff = now - 14400000L;
     
     try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
 
       
       String line = br.readLine();
       
       while ((line = br.readLine()) != null) {
         String[] c = line.split(",");
         if (c.length < 18)
           continue; 
         long ts = Long.parseLong(c[0]);
         if (ts < cutoff) {
           continue;
         }
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
 
         
         Metrics m = new Metrics(ts, c[1], c[2], Integer.parseInt(c[3]), Integer.parseInt(c[4]), (int)Math.round(Double.parseDouble(c[5])), Double.parseDouble(c[6]), Double.parseDouble(c[7]), Double.parseDouble(c[8]), Double.parseDouble(c[9]), Double.parseDouble(c[10]), Double.parseDouble(c[11]), Integer.parseInt(c[12]), Integer.parseInt(c[13]), Integer.parseInt(c[14]), Integer.parseInt(c[15]), Double.parseDouble(c[16]), Double.parseDouble(c[17]));
 
         
         this.fullHistory.computeIfAbsent(m.getServerName(), k -> new ArrayList());
         ((List<Metrics>)this.fullHistory.get(m.getServerName())).add(m);
       }
     
     } catch (Exception e) {
       e.printStackTrace();
     } 
   }
   
   public static class DataPoint { public long time;
     
     public DataPoint(long t, double v) {
       this.time = t; this.value = v;
     }
     public double value; }
   
   public Map<String, List<DataPoint>> get4hAveraged() {
     long now = System.currentTimeMillis();
     long start = now - 14400000L;
     long interval = 1800000L;
     
     List<DataPoint> cpu = new ArrayList<>();
     List<DataPoint> ram = new ArrayList<>();
     List<DataPoint> req = new ArrayList<>();
     long t;
     for (t = start; t <= now; t += interval) {
       
       double cpuSum = 0.0D, ramSum = 0.0D, reqSum = 0.0D;
       int count = 0;
       
       for (List<Metrics> list : this.fullHistory.values()) {
         for (Metrics m : list) {
           if (m.getTimestamp() >= t && m.getTimestamp() < t + interval) {
             cpuSum += m.getCpu();
             ramSum += m.getRam();
             reqSum += m.getReqPerSec();
             count++;
           } 
         } 
       } 
       
       if (count == 0) {
         cpu.add(new DataPoint(t, 0.0D));
         ram.add(new DataPoint(t, 0.0D));
         req.add(new DataPoint(t, 0.0D));
       } else {
         cpu.add(new DataPoint(t, cpuSum / count));
         ram.add(new DataPoint(t, ramSum / count));
         req.add(new DataPoint(t, reqSum / count));
       } 
     } 
     
     Map<String, List<DataPoint>> result = new HashMap<>();
     result.put("cpu", cpu);
     result.put("ram", ram);
     result.put("req", req);
     
     return result;
   }
   
   public Map<String, List<Metrics>> getFullHistory() {
     return this.fullHistory;
   }
 }


