package org.infy.profiler.monitoring;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.ThreadMXBean;
import org.infy.profiler.monitoring.MemoryMetrics;
import org.infy.profiler.monitoring.CpuMetrics;
import org.infy.profiler.monitoring.ThreadMetrics;

public class SystemMetricsCollector {
    private final MemoryMXBean memoryMXBean;
    private final OperatingSystemMXBean osMXBean;
    private final ThreadMXBean threadMXBean;
    
    public SystemMetricsCollector() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
        this.osMXBean = ManagementFactory.getOperatingSystemMXBean();
        this.threadMXBean = ManagementFactory.getThreadMXBean();
    }
    
    public MemoryMetrics collectMemoryMetrics() {
        return new MemoryMetrics(
            memoryMXBean.getHeapMemoryUsage().getUsed(),
            memoryMXBean.getHeapMemoryUsage().getMax(),
            memoryMXBean.getNonHeapMemoryUsage().getUsed(),
            memoryMXBean.getNonHeapMemoryUsage().getMax()
        );
    }
    
    public CpuMetrics collectCpuMetrics() {
        return new CpuMetrics(
            osMXBean.getSystemLoadAverage(),
            ((com.sun.management.OperatingSystemMXBean) osMXBean).getProcessCpuLoad(),
            Runtime.getRuntime().availableProcessors()
        );
    }
    
    public ThreadMetrics collectThreadMetrics() {
        return new ThreadMetrics(
            threadMXBean.getThreadCount(),
            threadMXBean.getPeakThreadCount(),
            threadMXBean.getDaemonThreadCount(),
            threadMXBean.getTotalStartedThreadCount()
        );
    }
} 