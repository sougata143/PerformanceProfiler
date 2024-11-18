package org.infy.profiler.interceptor;

import net.bytebuddy.asm.Advice;
import org.infy.profiler.PerformanceProfiler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MethodInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(MethodInterceptor.class);

    public static class TimingAdvice {
        @Advice.OnMethodEnter
        static long enter(@Advice.Origin("#t.#m") String methodName) {
            return System.nanoTime();
        }

        @Advice.OnMethodExit
        static void exit(@Advice.Origin("#t.#m") String methodName,
                        @Advice.Enter long startTime) {
            long executionTime = System.nanoTime() - startTime;
            try {
                PerformanceProfiler.getInstance().recordMethodExecution(methodName, executionTime);
            } catch (Exception e) {
                logger.error("Error recording method execution: " + methodName, e);
            }
        }
    }
} 