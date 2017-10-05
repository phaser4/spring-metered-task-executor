package net.phaser4.spring.concurrent;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("taskExecutor")
@Setter
public class TaskExecutorProperties {
    public int corePoolSize = 5;
    public int maxPoolSize = 10;
    public int queueCapacity = 1000;
}
