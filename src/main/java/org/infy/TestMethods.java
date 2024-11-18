package org.infy;

public class TestMethods {
    public void testMethod1() {
        // Simulate some work
        for (int i = 0; i < 1000000; i++) {
            Math.sqrt(i);
        }
    }
    
    public void testMethod2() {
        // Simulate different work
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10000; i++) {
            sb.append(i);
        }
    }
} 