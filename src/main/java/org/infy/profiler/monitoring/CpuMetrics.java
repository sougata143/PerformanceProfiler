package org.infy.profiler.monitoring;

public record CpuMetrics(
    double systemLoadAverage,
    double processCpuLoad,
    int availableProcessors
) {
    public double getNormalizedSystemLoad() {
        return systemLoadAverage / availableProcessors;
    }
} 