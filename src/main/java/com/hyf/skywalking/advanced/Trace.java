package com.hyf.skywalking.advanced;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.opentracing.Tracer;
import org.apache.skywalking.apm.meter.micrometer.SkywalkingConfig;
import org.apache.skywalking.apm.meter.micrometer.SkywalkingMeterRegistry;
import org.apache.skywalking.apm.toolkit.meter.Counter;
import org.apache.skywalking.apm.toolkit.meter.Gauge;
import org.apache.skywalking.apm.toolkit.meter.Histogram;
import org.apache.skywalking.apm.toolkit.meter.MeterFactory;
import org.apache.skywalking.apm.toolkit.opentracing.SkywalkingTracer;
import org.apache.skywalking.apm.toolkit.trace.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

/**
 * @author baB_hyf
 * @date 2021/06/13
 */
// @RestController
@RequestMapping("toolkit")
public class Trace {

    @org.apache.skywalking.apm.toolkit.trace.Trace
    @Tag(key = "tagName", value = "tagValue")
    @RequestMapping("trace")
    public String trace() {

        // 追踪API

        System.out.println("traceId: " + TraceContext.traceId());
        System.out.println("segmentId: " + TraceContext.segmentId());
        System.out.println("spanId: " + TraceContext.spanId());
        ActiveSpan.tag("tagName", "tagValue");
        ActiveSpan.info("span info message");

        TraceContext.putCorrelation("correlationKey", "correlationValue");
        // see in agent.config start with correlation.
        System.out.println("correlation: " + TraceContext.getCorrelation("correlation.value_max_length"));

        return "trace";
    }

    @RequestMapping("opentracing")
    public String openTracing() {

        // 实现 OpenTracing 规范

        Tracer tracer = new SkywalkingTracer();
        Tracer.SpanBuilder spanBuilder = tracer.buildSpan("/myapp/service");
        spanBuilder.startManual();

        return "opentracing";
    }

    @RequestMapping("meter")
    public String meter() {

        // 代表一个单调递增的计数器，自动收集数据并报告给后端
        Counter counter = MeterFactory.counter("counterName")
                .tag("tagKey", "tagValue").mode(Counter.Mode.RATE).build();
        counter.increment(1d);
        System.out.println("counter" + counter.get());

        // 表示单个数值
        // 1报错，1d没事
        Gauge gauge = MeterFactory.gauge("gaugeName", () -> 1d)
                .tag("tagKey", "tagValue").build();
        System.out.println("gauge: " + gauge.get());

        // 表示具有自定义存储桶的摘要样本观察
        Histogram histogram = MeterFactory.histogram("histogramName")
                .tag("tagKey", "tagValue").steps(Arrays.asList(1d, 5d, 10d)).minValue(0).build();
        histogram.addValue(3d);

        return "meter";
    }

    @RequestMapping("traceAccessThread")
    public String traceAccessThread() {

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        @TraceCrossThread
        class MyCallable implements Callable<Integer> {
            @Override
            public Integer call() throws Exception {
                System.out.println("MyCallable use @TraceCrossThread");
                return 1;
            }
        }

        executorService.submit(new MyCallable());
        executorService.submit(CallableWrapper.of(() -> 1)); // use CallableWrapper
        executorService.submit(RunnableWrapper.of(() -> System.out.println("use RunnableWrapper")));

        @TraceCrossThread
        class MySupplier implements Supplier<Integer> {
            @Override
            public Integer get() {
                System.out.println("MySupplier use @TraceCrossThread");
                return null;
            }
        }

        CompletableFuture.supplyAsync(new MySupplier());
        CompletableFuture.supplyAsync(SupplierWrapper.of(() -> 1)); // use SupplierWrapper

        return "opentracing";
    }

    @RequestMapping("skyWalkingMeterRegistry")
    public String skyWalkingMeterRegistry() {

        // micrometer整合

        SkywalkingMeterRegistry registry = new SkywalkingMeterRegistry();

        // If you has some counter want to rate by agent side
        SkywalkingConfig rateCounterConfig = new SkywalkingConfig(Arrays.asList("rate_counter_name"));
        new SkywalkingMeterRegistry(rateCounterConfig, Clock.SYSTEM);

        // Also you could using composite registry to combine multiple meter registry, such as collect to Skywalking and prometheus
        CompositeMeterRegistry compositeMeterRegistry = new CompositeMeterRegistry();
        compositeMeterRegistry.add(new SkywalkingMeterRegistry());
        // compositeMeterRegistry.add(new PrometheusMeterRegistry(PrometheusConfig.DEFAULT));


        // rate.counter.name will be send to rate_counter_name.
        // Millisecond unit.


        return "skyWalkingMeterRegistry";
    }
}
