 package com.monitoring.web;
 import com.monitoring.docker.DockerService;
 import com.monitoring.model.Metrics;
 import com.monitoring.model.Peak;
 import com.monitoring.model.ServerInfo;
 import com.monitoring.service.CsvLogger;
 import com.monitoring.service.MetricsFetcher;
 import com.monitoring.service.PeakDetector;
import java.io.File;
 import java.io.Serializable;
 import java.util.ArrayList;
 import java.util.LinkedHashMap;
 import java.util.List;
 import java.util.Map;
 import javax.annotation.PostConstruct;
 import javax.faces.bean.ManagedBean;
 import javax.faces.bean.ManagedProperty;
 import javax.faces.bean.ViewScoped;
 import org.primefaces.model.chart.Axis;
 import org.primefaces.model.chart.AxisType;
 import org.primefaces.model.chart.ChartSeries;
 import org.primefaces.model.chart.LineChartModel;
 import org.primefaces.model.chart.LineChartSeries;
 
 @ManagedBean(name = "monitorBean")
 @ViewScoped
 public class MonitorBean implements Serializable {
   private static final long serialVersionUID = 1L;
   @ManagedProperty("#{historyBean}")
   private HistoryBean historyBean;
   private List<ServerInfo> servers;
   private List<Metrics> tableData;
   private LineChartModel cpuChart;
   private LineChartModel ramChart;
   private LineChartModel reqChart;
   private String selectedServerName;
   
   public void setHistoryBean(HistoryBean hb) {
     this.historyBean = hb;
   }
 
 
 
 
 
 
 
   
   public String getSelectedServerName()
   {
     return this.selectedServerName; } public void setSelectedServerName(String s) {
     this.selectedServerName = s;
   }
   private LineChartModel singleCpuChart; private LineChartModel singleRamChart; private LineChartModel singleReqChart; private transient DockerService dockerService; private List<Metrics> singleServerMetrics = new ArrayList<>(); private transient MetricsFetcher fetcher;
   private transient CsvLogger csvLogger;
   private transient PeakDetector peakDetector;
   
   public List<Metrics> getSingleServerMetrics() {
     return this.singleServerMetrics; }
   public LineChartModel getSingleCpuChart() { return this.singleCpuChart; }
   public LineChartModel getSingleRamChart() { return this.singleRamChart; } public LineChartModel getSingleReqChart() {
     return this.singleReqChart;
   }
 
 
 
 
 
 
   
   @PostConstruct
   public void init() {
     String projectRoot = (new File(".")).getAbsolutePath();
     
     this.dockerService = new DockerService(projectRoot);
     this.fetcher = new MetricsFetcher();
     this.csvLogger = new CsvLogger(projectRoot);
     
     this.peakDetector = new PeakDetector(projectRoot);
     
     this.tableData = new ArrayList<>();
     
     try {
       this.servers = this.dockerService.getRunningServers();
     } catch (Exception e) {
       e.printStackTrace();
       this.servers = new ArrayList<>();
     } 
     
     buildCharts();
   }
 
   
   public void refresh() {
     this.tableData.clear();
     
     for (ServerInfo s : this.servers) {
       try {
         Metrics m = this.fetcher.fetch(s);
         this.csvLogger.log(m);
         
         ((List<Metrics>)this.historyBean.getFullHistory()
           .computeIfAbsent(s.getName(), k -> new ArrayList()))
           .add(m);
         
         this.tableData.add(m);
       }
       catch (Exception e) {
         e.printStackTrace();
       } 
     } 
     
     buildCharts();
   }
   
   public String createApache() {
     System.out.println("createApache() CALLED");
     try {
       ServerInfo s = this.dockerService.createApacheServer();
       this.servers.add(s);
       
       this.historyBean.getFullHistory().put(s.getName(), new ArrayList<>());
     }
     catch (Exception e) {
       e.printStackTrace();
     } 
     return "dashboard.xhtml";
   }
   
   public String createNginx() {
     System.out.println("createNginx() CALLED");
     try {
       ServerInfo s = this.dockerService.createNginxServer();
       this.servers.add(s);
       
       this.historyBean.getFullHistory().put(s.getName(), new ArrayList<>());
     }
     catch (Exception e) {
       e.printStackTrace();
     } 
     return "dashboard.xhtml";
   }
   
   public String goToServer() {
     if (this.selectedServerName == null || this.selectedServerName.isEmpty()) {
       return null;
     }
     return "/secured/server.xhtml?faces-redirect=true&name=" + this.selectedServerName;
   }
 
   
   public void loadSingleServer() {
     if (this.selectedServerName == null || this.selectedServerName.isEmpty()) {
       return;
     }
     List<Metrics> all = this.historyBean.getFullHistory().get(this.selectedServerName);
     
     if (all == null || all.isEmpty()) {
       this.singleServerMetrics = new ArrayList<>();
       this.singleCpuChart = new LineChartModel();
       this.singleRamChart = new LineChartModel();
       this.singleReqChart = new LineChartModel();
       
       return;
     } 
     this.singleServerMetrics = all;
     
     List<HistoryBean.DataPoint> cpu = new ArrayList<>();
     List<HistoryBean.DataPoint> ram = new ArrayList<>();
     List<HistoryBean.DataPoint> req = new ArrayList<>();
     
     for (Metrics m : all) {
       cpu.add(new HistoryBean.DataPoint(m.getTimestamp(), m.getCpu()));
       ram.add(new HistoryBean.DataPoint(m.getTimestamp(), m.getRam()));
       req.add(new HistoryBean.DataPoint(m.getTimestamp(), m.getReqPerSec()));
     } 
     
     this.singleCpuChart = buildSingleChart(cpu);
     this.singleRamChart = buildSingleChart(ram);
     this.singleReqChart = buildSingleChart(req);
   }
   
   public List<Peak> getPeaks() {
     return this.peakDetector.getPeaks();
   }
   
   public Map<String, List<Peak>> getPeaksByServer() {
     Map<String, List<Peak>> map = new LinkedHashMap<>();
     
     for (Peak p : this.peakDetector.getPeaks()) {
       ((List<Peak>)map.computeIfAbsent(p.getServerName(), k -> new ArrayList())).add(p);
     }
     
     return map;
   }
 
   
   private void buildCharts() {
     Map<String, List<HistoryBean.DataPoint>> avg = this.historyBean.get4hAveraged();
     
     this.cpuChart = buildSingleChart(avg.get("cpu"));
     this.ramChart = buildSingleChart(avg.get("ram"));
     this.reqChart = buildSingleChart(avg.get("req"));
   }
 
   
   private LineChartModel buildSingleChart(List<HistoryBean.DataPoint> data) {
     LineChartModel model = new LineChartModel();
     LineChartSeries s = new LineChartSeries();
     
     s.setShowMarker(false);
     s.setLabel("");
     
     int index = 0;
     for (HistoryBean.DataPoint p : data) {
       s.set(Integer.valueOf(index++), Double.valueOf(p.value));
     }
     
     model.addSeries((ChartSeries)s);
     
     model.setLegendPosition(null);
     
     Axis y = model.getAxis(AxisType.Y);
     y.setLabel("");
     y.setTickFormat("");
     y.setMin(Integer.valueOf(0));
     
     Axis x = model.getAxis(AxisType.X);
     x.setLabel("");
     x.setTickFormat("");
     x.setMin(Integer.valueOf(0));
     x.setMax(Integer.valueOf(8));
     x.setTickInterval("1");
     
     return model;
   }
   
   public LineChartModel getCpuChart() { return this.cpuChart; }
   public LineChartModel getRamChart() { return this.ramChart; }
   public LineChartModel getReqChart() { return this.reqChart; }
   public List<Metrics> getTableData() { return this.tableData; } public List<ServerInfo> getServers() {
     return this.servers;
   }
 }


