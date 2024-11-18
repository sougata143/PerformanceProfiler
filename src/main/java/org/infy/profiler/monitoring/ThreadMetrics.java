package org.infy.profiler.monitoring;

public record ThreadMetrics(
    int currentThreadCount,
    int peakThreadCount,
    int daemonThreadCount,
    long totalStartedThreadCount
) {} 