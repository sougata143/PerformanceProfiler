package org.infy;

import org.infy.profiler.PerformanceProfiler;
import org.infy.profiler.reporting.ReportGenerator;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Enable ByteBuddy experimental mode for newer Java versions
        System.setProperty("net.bytebuddy.experimental", "true");
        
        PerformanceProfiler profiler = PerformanceProfiler.getInstance();
        ReportGenerator reporter = new ReportGenerator(profiler);
        
        // Create reports directory
        String reportsPath = "reports";
        new File(reportsPath).mkdirs();
        
        // Create an instance of TestMethods to profile
        TestMethods testMethods = new TestMethods();
        
        // Start profiling the test methods class
        profiler.startProfiling(TestMethods.class.getName());
        
        // Show visualization
        reporter.showVisualization();
        
        // Run some test operations
        int iterations = 0;
        while (iterations < 10) { // Run for 10 iterations
            testMethods.testMethod1();
            testMethods.testMethod2();
            
            // Generate report every 5 iterations
            if (iterations % 5 == 0) {
                try {
                    reporter.generateReport(reportsPath);
                } catch (IOException e) {
                    System.err.println("Failed to generate report: " + e.getMessage());
                }
            }
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
            iterations++;
        }
        
        // Generate final report
        try {
            reporter.generateReport(reportsPath);
        } catch (IOException e) {
            System.err.println("Failed to generate final report: " + e.getMessage());
        }
        
        // Shutdown the profiler
        profiler.shutdown();
    }
}