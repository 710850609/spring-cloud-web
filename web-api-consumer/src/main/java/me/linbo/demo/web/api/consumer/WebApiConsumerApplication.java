package me.linbo.demo.web.api.consumer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableDiscoveryClient
@EnableFeignClients
@SpringBootApplication
        (scanBasePackages = {"me.linbo.api.core.exception", "me.linbo.demo.web.api.consumer"})
public class WebApiConsumerApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(WebApiConsumerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("启动完成");
    }
}

