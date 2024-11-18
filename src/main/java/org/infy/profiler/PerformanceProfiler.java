package org.infy.profiler;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.implementation.SuperMethodCall;
import net.bytebuddy.implementation.bind.annotation.Morph;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.asm.Advice;
import org.infy.profiler.interceptor.MethodInterceptor;
import org.infy.profiler.monitoring.SystemMetricsCollector;
import org.infy.profiler.monitoring.MemoryMetrics;
import org.infy.profiler.monitoring.CpuMetrics;
import org.infy.profiler.monitoring.ThreadMetrics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.HashMap;

public class PerformanceProfiler {
    private static final Logger logger = LoggerFactory.getLogger(PerformanceProfiler.class);
    private static final PerformanceProfiler INSTANCE = new PerformanceProfiler();
    
    private final ConcurrentHashMap<String, MethodMetrics> methodMetrics;
    private final Instrumentation instrumentation;
    private final SystemMetricsCollector metricsCollector;
    private final ScheduledExecutorService scheduler;
    private ProfilerConfig config;
    private MemoryMetrics latestMemoryMetrics;
    private CpuMetrics latestCpuMetrics;
    private ThreadMetrics latestThreadMetrics;
    
    private PerformanceProfiler() {
        this.methodMetrics = new ConcurrentHashMap<>();
        this.instrumentation = ByteBuddyAgent.install();
        this.metricsCollector = new SystemMetricsCollector();
        this.scheduler = Executors.newScheduledThreadPool(1);
        this.config = new ProfilerConfig.Builder().build();
        logger.info("Performance Profiler initialized");
    }
    
    public void configure(ProfilerConfig config) {
        this.config = config;
        startMetricsCollection();
    }
    
    private void startMetricsCollection() {
        scheduler.scheduleAtFixedRate(() -> {
            if (config.isCaptureMemoryMetrics()) {
                latestMemoryMetrics = metricsCollector.collectMemoryMetrics();
            }
            if (config.isCaptureCpuMetrics()) {
                latestCpuMetrics = metricsCollector.collectCpuMetrics();
            }
            if (config.isCaptureThreadMetrics()) {
                latestThreadMetrics = metricsCollector.collectThreadMetrics();
            }
        }, 0, config.getSamplingRate(), TimeUnit.MILLISECONDS);
    }
    
    public void startProfiling(String className) {
        try {
            Class<?> targetClass = Class.forName(className);
            new ByteBuddy()
                .redefine(targetClass)
                .visit(Advice.to(MethodInterceptor.TimingAdvice.class)
                    .on(ElementMatchers.any()
                        .and(ElementMatchers.not(ElementMatchers.isConstructor()))
                        .and(ElementMatchers.not(ElementMatchers.isStatic()))
                        .and(ElementMatchers.not(ElementMatchers.isSynthetic()))))
                .make()
                .load(targetClass.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            
            logger.info("Started profiling for class: {}", className);
        } catch (ClassNotFoundException e) {
            logger.error("Failed to start profiling for class: " + className, e);
        }
    }
    
    public void recordMethodExecution(String methodSignature, long executionTime) {
        methodMetrics.computeIfAbsent(methodSignature, MethodMetrics::new)
                    .recordExecution(executionTime);
    }
    
    public static PerformanceProfiler getInstance() {
        return INSTANCE;
    }
    
    public MethodMetrics getMetrics(String methodSignature) {
        return methodMetrics.get(methodSignature);
    }
    
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(60, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public Map<String, MethodMetrics> getAllMetrics() {
        return new HashMap<>(methodMetrics);
    }
    
    public MemoryMetrics getLatestMemoryMetrics() {
        return latestMemoryMetrics;
    }
    
    public CpuMetrics getLatestCpuMetrics() {
        return latestCpuMetrics;
    }
    
    public ThreadMetrics getLatestThreadMetrics() {
        return latestThreadMetrics;
    }
} 