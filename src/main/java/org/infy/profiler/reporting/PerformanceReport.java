package org.infy.profiler.reporting;

import org.infy.profiler.MethodMetrics;
import org.infy.profiler.monitoring.CpuMetrics;
import org.infy.profiler.monitoring.MemoryMetrics;
import org.infy.profiler.monitoring.ThreadMetrics;

import java.time.LocalDateTime;
import java.util.Map;

public class PerformanceReport {
    private final LocalDateTime timestamp;
    private final Map<String, MethodMetrics> methodMetrics;
    private final MemoryMetrics memoryMetrics;
    private final CpuMetrics cpuMetrics;
    private final ThreadMetrics threadMetrics;
    
    public PerformanceReport(Map<String, MethodMetrics> methodMetrics,
                           MemoryMetrics memoryMetrics,
                           CpuMetrics cpuMetrics,
                           ThreadMetrics threadMetrics) {
        this.timestamp = LocalDateTime.now();
        this.methodMetrics = methodMetrics;
        this.memoryMetrics = memoryMetrics;
        this.cpuMetrics = cpuMetrics;
        this.threadMetrics = threadMetrics;
    }
    
    // Getters
    public LocalDateTime getTimestamp() { return timestamp; }
    public Map<String, MethodMetrics> getMethodMetrics() { return methodMetrics; }
    public MemoryMetrics getMemoryMetrics() { return memoryMetrics; }
    public CpuMetrics getCpuMetrics() { return cpuMetrics; }
    public ThreadMetrics getThreadMetrics() { return threadMetrics; }
} 