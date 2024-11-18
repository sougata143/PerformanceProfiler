package org.infy.profiler.monitoring;

public record MemoryMetrics(
    long heapUsed,
    long heapMax,
    long nonHeapUsed,
    long nonHeapMax
) {
    public double getHeapUtilization() {
        return heapMax > 0 ? (double) heapUsed / heapMax : 0.0;
    }
} 