package com.learnclaude.library.actuator;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.metrics.MetricsEndpoint;
import org.springframework.stereotype.Service;

/**
 * MCP tools backed by Spring Boot Actuator. Each {@code @Tool} method is exposed
 * over the MCP streamable-HTTP endpoint (see application.yml) so an AI assistant
 * can ask about the running service's health and metrics.
 */
@Service
public class ActuatorTools {

    private final HealthEndpoint health;
    private final MetricsEndpoint metrics;

    public ActuatorTools(HealthEndpoint health, MetricsEndpoint metrics) {
        this.health = health;
        this.metrics = metrics;
    }

    @Tool(description = "Overall health status of the running service, e.g. UP or DOWN.")
    public String health() {
        return health.health().getStatus().getCode();
    }

    @Tool(description = "JVM uptime of the running service, in seconds.")
    public double jvmUptimeSeconds() {
        return metricValue("process.uptime");
    }

    @Tool(description = "Read a single Micrometer metric by name (e.g. 'jvm.memory.used', "
            + "'process.cpu.usage', 'process.uptime') and return its first measurement value.")
    public double metric(String name) {
        return metricValue(name);
    }

    private double metricValue(String name) {
        var descriptor = metrics.metric(name, null);
        if (descriptor == null || descriptor.getMeasurements().isEmpty()) {
            throw new IllegalArgumentException("No such metric: " + name);
        }
        return descriptor.getMeasurements().get(0).getValue();
    }
}
