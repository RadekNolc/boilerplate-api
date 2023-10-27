package com.radeknolc.apiname.general;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class GeneralBeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
