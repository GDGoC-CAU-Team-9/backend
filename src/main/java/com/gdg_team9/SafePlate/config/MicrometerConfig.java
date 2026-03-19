package com.gdg_team9.SafePlate.config;

import io.micrometer.core.aop.TimedAspect;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class MicrometerConfig {
    @Bean
    public MeterFilter cloudWatchMeterFilter() {
        Set<String> allowedSystems = Set.of(
                "jvm.threads.states",
                "logback.events"
        );
        return MeterFilter.denyUnless(id ->
                allowedSystems.contains(id.getName()) || id.getName().startsWith("safeplate.")
        );
    }

    @Bean
    public TimedAspect timedAspect(MeterRegistry registry) {
        return new TimedAspect(registry);
    }
}
