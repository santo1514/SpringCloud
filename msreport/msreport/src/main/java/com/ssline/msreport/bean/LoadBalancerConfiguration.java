package com.ssline.msreport.bean;

import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class LoadBalancerConfiguration {

    public ServiceInstanceListSupplier serviceInstanceListSupplier(ConfigurableApplicationContext context) {
        log.info("Configuring Load Balancer to allow multiple instances");
        return ServiceInstanceListSupplier.builder()
                .withBlockingDiscoveryClient()
                //.withSameInstancePreference()
                .build(context);
    }

}