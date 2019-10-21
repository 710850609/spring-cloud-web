package me.linbo.demo.web.api.consumer;

import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

/**
 * @author LinBo
 * @date 2019-10-21 15:48
 */
@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountApi {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("")
    public Response<PageDto<Account>> list(AccountQueryDto params) {
        log.info("请求查询列表:{}", params);
        ResponseEntity<Response> entity = restTemplate.getForEntity("http://web-api/accounts", Response.class);
        return entity.getBody();
    }
}
