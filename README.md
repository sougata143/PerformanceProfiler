# Performance Profiler

A lightweight Java performance profiling tool that provides real-time monitoring, visualization, and analysis of application performance metrics.

## Features

- Real-time method execution monitoring
- Memory usage tracking
- CPU utilization monitoring
- Thread statistics
- Interactive visualization dashboard
- Method performance analysis
- Exportable performance reports

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- IDE (IntelliJ IDEA, Eclipse, or similar)

## Building the Project

1. Clone the repository: 
2. Build with Maven: mvn clean install


## Running the Profiler

### Method 1: Using Maven
mvn exec:java


### Method 2: Using Java directly
java -Dnet.bytebuddy.experimental=true -jar target/PerformanceProfiler-1.0-SNAPSHOT.jar


### Method 3: From your IDE
Run the `Main` class with VM argument: `-Dnet.bytebuddy.experimental=true`

## Usage in Your Project

1. Add the profiler as a dependency in your project's pom.xml:
   <dependency>
        <groupId>org.infy</groupId>
        <artifactId>PerformanceProfiler</artifactId>
        <version>1.0-SNAPSHOT</version>      
   </dependency>
2. Initialize and start the profiler:
   Java
   // Enable ByteBuddy experimental mode
   System.setProperty("net.bytebuddy.experimental", "true");
   // Get profiler instance
   PerformanceProfiler profiler = PerformanceProfiler.getInstance();
   // Create visualization
   ReportGenerator reporter = new ReportGenerator(profiler);
   // Start profiling a class
   profiler.startProfiling("com.yourpackage.YourClass");
   // Show visualization dashboard
   reporter.showVisualization();


## Configuration

You can configure the profiler using the builder pattern:
ProfilerConfig config = new ProfilerConfig.Builder()
.samplingRate(200) // Set sampling rate in milliseconds
.captureMemoryMetrics(true) // Enable/disable memory metrics
.captureCpuMetrics(true) // Enable/disable CPU metrics
.captureThreadMetrics(true) // Enable/disable thread metrics
.build();
profiler.configure(config);


## Visualization Dashboard

The dashboard provides:
- Real-time memory usage graph
- CPU utilization timeline
- Top methods by execution time
- Detailed method performance table
- Status bar with current metrics

## Generating Reports
java
ReportGenerator reporter = new ReportGenerator(profiler);
reporter.generateReport("./reports");


Reports are generated in JSON format and include:
- Timestamp
- Method execution metrics
- Memory metrics
- CPU metrics
- Thread metrics

## Best Practices

1. **Memory Considerations**
    - The profiler maintains metrics in memory
    - Consider periodic report generation for long-running applications
    - Use appropriate sampling rates for your use case

2. **Performance Impact**
    - The profiler adds minimal overhead
    - Use selective profiling for specific classes
    - Adjust sampling rate based on your needs

3. **Production Usage**
    - Not recommended for production environments
    - Use in development and testing environments
    - Consider security implications of exposed metrics

## Troubleshooting

1. **ByteBuddy Errors**
    - Ensure `-Dnet.bytebuddy.experimental=true` is set
    - Check Java version compatibility
    - Verify class access permissions

2. **Visualization Issues**
    - Ensure Swing/AWT environment is available
    - Check for null metrics in updates
    - Verify memory availability for charts

3. **Common Issues**
    - ClassNotFoundException: Verify class names and packages
    - Memory warnings: Adjust sampling rate
    - UI freezes: Check update frequency

## Contributing

1. Fork the repository
2. Create your feature branch
3. Commit your changes
4. Push to the branch
5. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For support, please open an issue in the GitHub repository or contact the maintainers.
