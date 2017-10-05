package net.phaser4.spring.concurrent

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.actuate.endpoint.MetricsEndpoint
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async
import org.springframework.scheduling.annotation.EnableAsync
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestPropertySource
import spock.lang.Specification

import java.util.concurrent.CountDownLatch

import static net.phaser4.spring.concurrent.ThreadPoolExecutorMetrics.METRIC_NAME_EXECUTOR_ACTIVE_THREADS
import static net.phaser4.spring.concurrent.ThreadPoolExecutorMetrics.METRIC_NAME_EXECUTOR_QUEUE_SIZE

@ContextConfiguration(classes = [TestConfiguration])
@TestPropertySource(properties = ["taskExecutor.corePoolSize=1", "taskExecutor.maxPoolSize=1"])
class IntegrationTest extends Specification {

    @Autowired
    SampleClass sampleClass

    @Autowired
    MetricsEndpoint metricsEndpoint

    def "should return the queue size and active threads metrics"() {
        expect:
        getQueueSizeMetricValue() == 0
        getActiveThreadsMetricValue() == 0

        when:
        sampleClass.sampleWaitingMethod()
        sampleClass.sampleMethod()

        then:
        getQueueSizeMetricValue() == 1
        getActiveThreadsMetricValue() == 1

        cleanup:
        sampleClass.latch.countDown()
    }

    private Integer getQueueSizeMetricValue() {
        getMetricValue(METRIC_NAME_EXECUTOR_QUEUE_SIZE)
    }

    private Integer getActiveThreadsMetricValue() {
        getMetricValue(METRIC_NAME_EXECUTOR_ACTIVE_THREADS)
    }

    private int getMetricValue(metricName) {
        metricsEndpoint.invoke().find { it.key == metricName }.value as Integer
    }

    static class SampleClass {
        def latch = new CountDownLatch(1)

        @Async
        def sampleWaitingMethod() { latch.await() }

        @Async
        def sampleMethod() {}
    }

    @Configuration
    @EnableAsync
    @EnableAutoConfiguration
    static class TestConfiguration {
        @Bean
        SampleClass sampleClass() { new SampleClass() }
    }
}
