package cz.radeknolc.appname.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

@Configuration
public class SharedBeanConfiguration {

    @Bean
    Clock clock() {
        return Clock.systemDefaultZone();
    }
}
