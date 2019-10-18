package me.linbo.web.demo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;

@SpringBootApplication
@Slf4j
@RefreshScope
@EnableDiscoveryClient
public class WebApiApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebApiApplication.class, args);
    }

    @Autowired
    Environment environment;

    @Override
    public void run(String... args) throws Exception {
        log.info("启动完成");
    }
}


