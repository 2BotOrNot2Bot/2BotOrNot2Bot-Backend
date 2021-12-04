package com.turing.test.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
* @Author Yibo Wen
* @Date 2021/10/24 15:23
**/
@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor taskExecutor() {
        log.info("AsyncConfig->taskExecutor: Creating Async Task Executor");
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        // core thread: thread number of threads initialized during thread pool
        executor.setCorePoolSize(4);
        // maximum thread: The maximum number of thread pools, only after the buffer queue is full,
        // the thread exceeding the core thread is applied.
        executor.setMaxPoolSize(8);
        // buffer queue: queue used to buffer execution tasks
        executor.setQueueCapacity(20);
        // Allow thread free time 60 seconds: When the thread beyond the core thread is destroyed
        // after the idle time arrives
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("TuringTestThread-");
        executor.initialize();
        return executor;
    }

}
