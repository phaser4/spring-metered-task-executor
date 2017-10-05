package net.phaser4.spring.concurrent

import org.springframework.boot.actuate.metrics.Metric
import spock.lang.Specification

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor

import static net.phaser4.spring.concurrent.ThreadPoolExecutorMetrics.METRIC_NAME_EXECUTOR_ACTIVE_THREADS
import static net.phaser4.spring.concurrent.ThreadPoolExecutorMetrics.METRIC_NAME_EXECUTOR_QUEUE_SIZE

class ThreadPoolExecutorMetricsTest extends Specification {

    ThreadPoolExecutor executor
    ThreadPoolExecutorMetrics metrics

    def setup() {
        executor = Mock(ThreadPoolExecutor)
        metrics = new ThreadPoolExecutorMetrics(executor)
    }

    def "should return metric with the queue size and the active threads count"() {
        when:
        def result = metrics.metrics()

        then:
        1 * executor.getQueue() >> ([1, 2] as LinkedBlockingQueue)
        1 * executor.getActiveCount() >> 5
        0 * _

        result.size() == 2
        findMetric(result, METRIC_NAME_EXECUTOR_QUEUE_SIZE) == 2
        findMetric(result, METRIC_NAME_EXECUTOR_ACTIVE_THREADS) == 5
    }

    def findMetric(Collection<Metric> metrics, String name) {
        metrics.find { it.name == name }.value
    }
}
