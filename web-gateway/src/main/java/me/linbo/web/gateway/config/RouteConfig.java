package me.linbo.web.gateway.config;

import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author LinBo
 * @date 2019-10-15 16:27
 */
//@Configuration
public class RouteConfig {

    @Bean
    public RouteLocator routeLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                // 重定向
                .route("rewrite_route", r -> r.path("/redirect").filters(f -> f.rewritePath("/redirect/(?<segment).*", "/${segment}")).uri("http://localhost"))
                // 熔断
                .route("hystrix_fallback_route", r -> r.host("*.hystrixfallback.org")
                        .filters(f -> f.hystrix(c -> c.setName("slowcmd").setFallbackUri("forward:/hystrixfallback")))
                        .uri("http://httpbin.org"))
                // 限流
                .route("limit_route", r -> r
                        .host("*.limited.org").and().path("/**")
                        .filters(f -> f.requestRateLimiter(c -> c.setRateLimiter(redisRateLimiter())))
                        .uri("http://httpbin.org"))
                .build();
    }

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        return new RedisRateLimiter(1, 2);
    }

    @RequestMapping("/hystrixfallback")
    public String hystrixfallback() {
        return "This is a fallback";
    }

    @Bean
    SecurityWebFilterChain springWebFilterChain(ServerHttpSecurity http) throws Exception {
        return http.httpBasic().and()
                .csrf().disable()
                .authorizeExchange()
//                .pathMatchers("/**").authenticated()
                .anyExchange().permitAll()
                .and()
                .build();
    }

    @Bean
    public MapReactiveUserDetailsService reactiveUserDetailsService() {
        UserDetails user = User.builder().username("linbo").password("123").roles("ADMIN").build();
        return new MapReactiveUserDetailsService(user);
    }
}
