package com.ssline.company.config;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import io.micrometer.observation.Observation;
import io.micrometer.observation.Observation.Context;
import lombok.extern.slf4j.Slf4j;
import io.micrometer.observation.ObservationHandler;

@Component
@Order(999)
@Slf4j
public class LogObservationHandler implements ObservationHandler<Observation.Context>{

    @Override
    public void onStart(Context context) {
        log.info("Observation started: {}", context.getName());
    }

    @Override
    public void onError(Context context) {
        log.error("Observation error: {}", context.getError().getMessage());
    }

    @Override
    public void onEvent(Observation.Event event, Context context) {
        log.info("Observation event: {}", event.getName());
    }

    @Override
    public void onStop(Context context) {
        log.info("Observation stopped: {}", context.getName());
    }

    @Override
    public boolean supportsContext(Observation.Context context) {
        return true;
    }
}
