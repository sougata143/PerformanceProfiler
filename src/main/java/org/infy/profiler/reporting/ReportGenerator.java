package org.infy.profiler.reporting;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.infy.profiler.PerformanceProfiler;
import org.infy.profiler.visualization.PerformanceVisualizer;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ReportGenerator {
    private final PerformanceProfiler profiler;
    private final ObjectMapper objectMapper;
    private PerformanceVisualizer visualizer;
    
    public ReportGenerator(PerformanceProfiler profiler) {
        this.profiler = profiler;
        this.objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    }
    
    public void generateReport(String outputPath) throws IOException {
        PerformanceReport report = new PerformanceReport(
            profiler.getAllMetrics(),
            profiler.getLatestMemoryMetrics(),
            profiler.getLatestCpuMetrics(),
            profiler.getLatestThreadMetrics()
        );
        
        // Generate JSON report
        String filename = String.format("performance_report_%s.json",
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")));
        File outputFile = new File(outputPath, filename);
        objectMapper.writeValue(outputFile, report);
    }
    
    public void showVisualization() {
        if (visualizer == null) {
            visualizer = new PerformanceVisualizer();
        }
        
        visualizer.updateData(
            profiler.getLatestMemoryMetrics(),
            profiler.getLatestCpuMetrics(),
            profiler.getAllMetrics()
        );
        
        visualizer.setVisible(true);
    }
} 