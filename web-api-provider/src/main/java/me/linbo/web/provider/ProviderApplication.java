package me.linbo.web.provider;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.core.env.Environment;

@Slf4j
@EnableDiscoveryClient
@SpringBootApplication
        (scanBasePackages = {"me.linbo.api.core.exception", "me.linbo.web.provider"})
public class ProviderApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(ProviderApplication.class, args);
    }

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        log.info("启动完成");
    }
}


