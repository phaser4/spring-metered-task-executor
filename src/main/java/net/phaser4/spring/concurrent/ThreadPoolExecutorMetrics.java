package net.phaser4.spring.concurrent;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.endpoint.PublicMetrics;
import org.springframework.boot.actuate.metrics.Metric;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;

@AllArgsConstructor
public class ThreadPoolExecutorMetrics implements PublicMetrics {

    public final static String METRIC_NAME_EXECUTOR_QUEUE_SIZE = "taskExecutor.queueSize";
    public final static String METRIC_NAME_EXECUTOR_ACTIVE_THREADS = "taskExecutor.activeThreads";

    private final ThreadPoolExecutor executor;

    @Override
    public Collection<Metric<?>> metrics() {
        List<Metric<?>> metrics = new ArrayList<>();
        metrics.add(new Metric<Integer>(METRIC_NAME_EXECUTOR_QUEUE_SIZE, executor.getQueue().size()));
        metrics.add(new Metric<Integer>(METRIC_NAME_EXECUTOR_ACTIVE_THREADS, executor.getActiveCount()));
        return metrics;
    }
}
