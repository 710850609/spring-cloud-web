package me.linbo.demo.web.api.consumer.config;

import feign.Contract;
import feign.Logger;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.cloud.openfeign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LinBo
 * @date 2019-10-23 13:21
 */
@Configuration
public class FeignClientConfig {

//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
////        return new SpringMvcContract();
//    }

//    @Bean
//    public RequestInterceptor headerInterceptor() {
//        return new RequestInterceptor() {
//            @Override
//            public void apply(RequestTemplate requestTemplate) {
//                requestTemplate.header("Content-Type", "application/json");
//            }
//        };
//    }

    @Bean
    public Logger.Level feginLoggerLevel() {
        return Logger.Level.FULL;
    }

}
