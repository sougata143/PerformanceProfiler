package org.infy.profiler;

public class ProfilerConfig {
    private int samplingRate = 100; // Default sampling rate in milliseconds
    private boolean captureMemoryMetrics = true;
    private boolean captureCpuMetrics = true;
    private boolean captureThreadMetrics = true;
    
    // Builder pattern
    public static class Builder {
        private final ProfilerConfig config;
        
        public Builder() {
            config = new ProfilerConfig();
        }
        
        public Builder samplingRate(int rate) {
            config.samplingRate = rate;
            return this;
        }
        
        public Builder captureMemoryMetrics(boolean capture) {
            config.captureMemoryMetrics = capture;
            return this;
        }
        
        public Builder captureCpuMetrics(boolean capture) {
            config.captureCpuMetrics = capture;
            return this;
        }
        
        public Builder captureThreadMetrics(boolean capture) {
            config.captureThreadMetrics = capture;
            return this;
        }
        
        public ProfilerConfig build() {
            return config;
        }
    }
    
    // Getters
    public int getSamplingRate() {
        return samplingRate;
    }
    
    public boolean isCaptureMemoryMetrics() {
        return captureMemoryMetrics;
    }
    
    public boolean isCaptureCpuMetrics() {
        return captureCpuMetrics;
    }
    
    public boolean isCaptureThreadMetrics() {
        return captureThreadMetrics;
    }
} 