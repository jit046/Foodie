package com.example.todo.monitoring;

import org.springframework.stereotype.Service;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.util.HashMap;
import java.util.Map;

/**
 * Custom Memory Monitoring Service for MCP Integration
 * Provides detailed memory metrics that can be accessed via MCP server
 */
@Service
public class MemoryMonitoringService {

    private final MemoryMXBean memoryMXBean;

    public MemoryMonitoringService() {
        this.memoryMXBean = ManagementFactory.getMemoryMXBean();
    }

    public double getHeapUsed() {
        return memoryMXBean.getHeapMemoryUsage().getUsed();
    }

    public double getHeapMax() {
        return memoryMXBean.getHeapMemoryUsage().getMax();
    }

    public double getHeapUsagePercentage() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        return (double) heapUsage.getUsed() / heapUsage.getMax() * 100;
    }

    public double getNonHeapUsed() {
        return memoryMXBean.getNonHeapMemoryUsage().getUsed();
    }

    /**
     * Get comprehensive memory statistics for MCP consumption
     */
    public Map<String, Object> getDetailedMemoryStats() {
        MemoryUsage heapUsage = memoryMXBean.getHeapMemoryUsage();
        MemoryUsage nonHeapUsage = memoryMXBean.getNonHeapMemoryUsage();

        Map<String, Object> stats = new HashMap<>();
        
        // Heap Memory Stats
        Map<String, Object> heapStats = new HashMap<>();
        heapStats.put("used_mb", heapUsage.getUsed() / (1024.0 * 1024.0));
        heapStats.put("max_mb", heapUsage.getMax() / (1024.0 * 1024.0));
        heapStats.put("committed_mb", heapUsage.getCommitted() / (1024.0 * 1024.0));
        heapStats.put("usage_percentage", (double) heapUsage.getUsed() / heapUsage.getMax() * 100);
        
        // Non-Heap Memory Stats
        Map<String, Object> nonHeapStats = new HashMap<>();
        nonHeapStats.put("used_mb", nonHeapUsage.getUsed() / (1024.0 * 1024.0));
        nonHeapStats.put("committed_mb", nonHeapUsage.getCommitted() / (1024.0 * 1024.0));
        
        stats.put("heap", heapStats);
        stats.put("non_heap", nonHeapStats);
        stats.put("timestamp", System.currentTimeMillis());
        
        return stats;
    }
}
