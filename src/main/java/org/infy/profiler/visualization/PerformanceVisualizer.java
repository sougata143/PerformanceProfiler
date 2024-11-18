package org.infy.profiler.visualization;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.infy.profiler.MethodMetrics;
import org.infy.profiler.monitoring.CpuMetrics;
import org.infy.profiler.monitoring.MemoryMetrics;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PerformanceVisualizer extends JFrame {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceVisualizer.class);
    private final TimeSeries memoryUsageSeries;
    private final TimeSeries cpuUsageSeries;
    private final DefaultCategoryDataset methodExecutionDataset;
    private final MethodMetricsTable methodMetricsTable;
    private final JLabel statusLabel;
    private static final double DEFAULT_MEMORY_VALUE = 0.0;
    private static final double DEFAULT_CPU_VALUE = 0.0;
    
    public PerformanceVisualizer() {
        super("Performance Profiler Visualization");
        
        this.memoryUsageSeries = new TimeSeries("Memory Usage");
        this.cpuUsageSeries = new TimeSeries("CPU Usage");
        this.methodExecutionDataset = new DefaultCategoryDataset();
        this.methodMetricsTable = new MethodMetricsTable();
        this.statusLabel = new JLabel("Status: Running");
        
        initializeUI();
    }
    
    private void initializeUI() {
        setLayout(new BorderLayout());
        setSize(1400, 900);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // Create main split pane
        JSplitPane mainSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        mainSplitPane.setResizeWeight(0.6);
        
        // Top panel with charts
        JPanel chartsPanel = new JPanel(new GridLayout(1, 2));
        chartsPanel.add(createMemoryAndCpuPanel());
        chartsPanel.add(createMethodExecutionChart());
        mainSplitPane.setTopComponent(chartsPanel);
        
        // Bottom panel with table
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.add(new JLabel("Method Performance Details", SwingConstants.CENTER), 
                       BorderLayout.NORTH);
        bottomPanel.add(methodMetricsTable, BorderLayout.CENTER);
        mainSplitPane.setBottomComponent(bottomPanel);
        
        // Add components to frame
        add(mainSplitPane, BorderLayout.CENTER);
        add(createStatusBar(), BorderLayout.SOUTH);
    }
    
    private JPanel createMemoryAndCpuPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        panel.add(createMemoryChart());
        panel.add(createCpuChart());
        return panel;
    }
    
    private ChartPanel createMemoryChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection(memoryUsageSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "Memory Usage Over Time",
            "Time",
            "Memory (MB)",
            dataset,
            true,
            true,
            false
        );
        return new ChartPanel(chart);
    }
    
    private ChartPanel createCpuChart() {
        TimeSeriesCollection dataset = new TimeSeriesCollection(cpuUsageSeries);
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
            "CPU Usage Over Time",
            "Time",
            "CPU Usage (%)",
            dataset,
            true,
            true,
            false
        );
        return new ChartPanel(chart);
    }
    
    private ChartPanel createMethodExecutionChart() {
        JFreeChart chart = ChartFactory.createBarChart(
            "Top Methods by Execution Time",
            "Method",
            "Average Time (ms)",
            methodExecutionDataset,
            PlotOrientation.HORIZONTAL,
            true,
            true,
            false
        );
        return new ChartPanel(chart);
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());
        statusBar.add(statusLabel, BorderLayout.WEST);
        return statusBar;
    }
    
    public void updateData(MemoryMetrics memoryMetrics, CpuMetrics cpuMetrics, 
                         Map<String, MethodMetrics> methodMetrics) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Update time series
                Second current = new Second();
                
                // Safely handle memory metrics
                double memoryValue = DEFAULT_MEMORY_VALUE;
                if (memoryMetrics != null) {
                    memoryValue = memoryMetrics.heapUsed() / (1024.0 * 1024.0);
                }
                memoryUsageSeries.add(current, memoryValue);
                
                // Safely handle CPU metrics
                double cpuValue = DEFAULT_CPU_VALUE;
                if (cpuMetrics != null) {
                    cpuValue = cpuMetrics.processCpuLoad() * 100;
                }
                cpuUsageSeries.add(current, cpuValue);
                
                // Update method execution chart (top 10 methods)
                methodExecutionDataset.clear();
                if (methodMetrics != null) {
                    methodMetrics.entrySet().stream()
                        .sorted((e1, e2) -> Double.compare(
                            e2.getValue().getAverageExecutionTime(),
                            e1.getValue().getAverageExecutionTime()))
                        .limit(10)
                        .forEach(entry -> methodExecutionDataset.addValue(
                            entry.getValue().getAverageExecutionTime() / 1_000_000.0,
                            "Avg Execution Time",
                            entry.getKey()
                        ));
                    
                    // Update method metrics table
                    methodMetricsTable.updateData(methodMetrics);
                }
                
                // Update status with safe values
                String statusText = String.format("Status: Running | Memory: %.1f MB | CPU: %.1f%%",
                    memoryValue,
                    cpuValue);
                
                if (memoryMetrics == null || cpuMetrics == null) {
                    statusText = "Status: Waiting for metrics...";
                }
                
                statusLabel.setText(statusText);
                
            } catch (Exception e) {
                logger.error("Error updating visualization", e);
                statusLabel.setText("Status: Error updating metrics");
            }
        });
    }
} 