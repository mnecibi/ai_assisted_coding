package com.learnclaude.library.actuator;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Registers {@link ActuatorTools} with the MCP server so its {@code @Tool}
 * methods are advertised to MCP clients.
 */
@Configuration
public class ActuatorToolsConfig {

    @Bean
    public ToolCallbackProvider actuatorToolCallbacks(ActuatorTools actuatorTools) {
        return MethodToolCallbackProvider.builder().toolObjects(actuatorTools).build();
    }
}
