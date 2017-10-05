package net.phaser4.spring.concurrent;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableConfigurationProperties(TaskExecutorProperties.class)
public class TaskExecutorConfiguration {
    @Bean
    public ThreadPoolExecutorMetrics queueMetrics(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        return new ThreadPoolExecutorMetrics(threadPoolTaskExecutor.getThreadPoolExecutor());
    }

    @Bean
    public ThreadPoolTaskExecutor taskExecutor(TaskExecutorProperties properties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(properties.corePoolSize);
        executor.setMaxPoolSize(properties.maxPoolSize);
        executor.setQueueCapacity(properties.queueCapacity);
        return executor;
    }
}
