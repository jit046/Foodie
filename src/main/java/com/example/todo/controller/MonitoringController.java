package com.example.todo.controller;

import com.example.todo.monitoring.MemoryMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Monitoring endpoints specifically designed for MCP server integration
 * These endpoints provide structured data that MCP can easily consume
 */
@RestController
@RequestMapping("/api/monitoring")
public class MonitoringController {

    private final MemoryMonitoringService memoryMonitoringService;

    @Autowired
    public MonitoringController(MemoryMonitoringService memoryMonitoringService) {
        this.memoryMonitoringService = memoryMonitoringService;
    }

    /**
     * Get detailed memory statistics for MCP consumption
     */
    @GetMapping("/memory")
    public Map<String, Object> getMemoryStats() {
        return memoryMonitoringService.getDetailedMemoryStats();
    }

    /**
     * Get garbage collection statistics
     */
    @GetMapping("/gc")
    public Map<String, Object> getGCStats() {
        List<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
        Map<String, Object> gcStats = new HashMap<>();
        
        for (GarbageCollectorMXBean gcBean : gcBeans) {
            Map<String, Object> collectorStats = new HashMap<>();
            collectorStats.put("collection_count", gcBean.getCollectionCount());
            collectorStats.put("collection_time_ms", gcBean.getCollectionTime());
            collectorStats.put("memory_pool_names", gcBean.getMemoryPoolNames());
            
            gcStats.put(gcBean.getName(), collectorStats);
        }
        
        gcStats.put("timestamp", System.currentTimeMillis());
        return gcStats;
    }

    /**
     * Get application health status for MCP monitoring
     */
    @GetMapping("/health")
    public Map<String, Object> getHealthStatus() {
        Map<String, Object> health = new HashMap<>();
        
        // Memory health check
        double heapUsagePercent = memoryMonitoringService.getHeapUsagePercentage();
        String memoryStatus = heapUsagePercent < 80 ? "HEALTHY" : 
                             heapUsagePercent < 90 ? "WARNING" : "CRITICAL";
        
        health.put("memory_status", memoryStatus);
        health.put("heap_usage_percent", heapUsagePercent);
        health.put("uptime_ms", ManagementFactory.getRuntimeMXBean().getUptime());
        health.put("thread_count", ManagementFactory.getThreadMXBean().getThreadCount());
        health.put("timestamp", System.currentTimeMillis());
        
        return health;
    }

    /**
     * Get comprehensive system overview for MCP dashboard
     */
    @GetMapping("/overview")
    public Map<String, Object> getSystemOverview() {
        Map<String, Object> overview = new HashMap<>();
        
        // Include all monitoring data
        overview.put("memory", getMemoryStats());
        overview.put("gc", getGCStats());
        overview.put("health", getHealthStatus());
        
        // Add JVM info
        Map<String, Object> jvmInfo = new HashMap<>();
        jvmInfo.put("java_version", System.getProperty("java.version"));
        jvmInfo.put("jvm_name", ManagementFactory.getRuntimeMXBean().getVmName());
        jvmInfo.put("start_time", ManagementFactory.getRuntimeMXBean().getStartTime());
        
        overview.put("jvm_info", jvmInfo);
        overview.put("timestamp", System.currentTimeMillis());
        
        return overview;
    }
}
