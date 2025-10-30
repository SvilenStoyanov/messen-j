package com.svistoyanov.mj;

import com.svistoyanov.mj.config.jms.JmsConfiguration;
import com.svistoyanov.mj.config.spring.PersistenceConfig;
import com.svistoyanov.mj.config.spring.RestConfig;
import com.svistoyanov.mj.config.spring.WorkerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "com.svistoyanov.mj.*"))
@Import({
        PersistenceConfig.class,
        RestConfig.class,
        JmsConfiguration.class,
        WorkerConfiguration.class
})
@EnableConfigurationProperties({
        JmsProperties.class
})
public class SpringBootApp {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
}
