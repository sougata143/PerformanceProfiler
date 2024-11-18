package org.infy.profiler;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class MethodMetrics {
    private final String methodName;
    private final LongAdder invocationCount;
    private final AtomicLong totalExecutionTime;
    private final AtomicLong maxExecutionTime;
    private final AtomicLong minExecutionTime;
    
    public MethodMetrics(String methodName) {
        this.methodName = methodName;
        this.invocationCount = new LongAdder();
        this.totalExecutionTime = new AtomicLong(0);
        this.maxExecutionTime = new AtomicLong(Long.MIN_VALUE);
        this.minExecutionTime = new AtomicLong(Long.MAX_VALUE);
    }
    
    public void recordExecution(long executionTime) {
        invocationCount.increment();
        totalExecutionTime.addAndGet(executionTime);
        updateMaxExecutionTime(executionTime);
        updateMinExecutionTime(executionTime);
    }
    
    private void updateMaxExecutionTime(long executionTime) {
        long currentMax;
        do {
            currentMax = maxExecutionTime.get();
            if (executionTime <= currentMax) {
                break;
            }
        } while (!maxExecutionTime.compareAndSet(currentMax, executionTime));
    }
    
    private void updateMinExecutionTime(long executionTime) {
        long currentMin;
        do {
            currentMin = minExecutionTime.get();
            if (executionTime >= currentMin) {
                break;
            }
        } while (!minExecutionTime.compareAndSet(currentMin, executionTime));
    }
    
    // Getters
    public String getMethodName() {
        return methodName;
    }
    
    public long getInvocationCount() {
        return invocationCount.sum();
    }
    
    public long getTotalExecutionTime() {
        return totalExecutionTime.get();
    }
    
    public long getAverageExecutionTime() {
        long count = getInvocationCount();
        return count > 0 ? totalExecutionTime.get() / count : 0;
    }
    
    public long getMaxExecutionTime() {
        return maxExecutionTime.get();
    }
    
    public long getMinExecutionTime() {
        return minExecutionTime.get();
    }
} 