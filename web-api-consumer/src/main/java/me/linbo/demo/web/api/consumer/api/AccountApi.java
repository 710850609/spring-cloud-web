package me.linbo.demo.web.api.consumer.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.demo.web.api.consumer.model.Account;
import me.linbo.demo.web.api.consumer.model.AccountQueryDto;
import me.linbo.demo.web.api.consumer.service.others.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

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

    @Autowired
    private AccountService accountService;

    @GetMapping("restTemplate")
    public Response<PageDto<Account>> list(AccountQueryDto params) {
        log.info("请求查询列表:{}", params);
        ObjectMapper mapper = new ObjectMapper();
        Map map = mapper.convertValue(params, Map.class);
        ResponseEntity<Response> entity = restTemplate.getForEntity("http://web-api/accounts?pageNo={pageNo}&pageSize={pageSize}&id={id}", Response.class, map);
        return entity.getBody();
    }

    @GetMapping("/openFeign")
    public Response<PageDto<Account>> openFeign(AccountQueryDto params) {
        log.info("请求查询列表:{}", params);
        return accountService.list(params);
    }

    @GetMapping("/openFeign/{id}")
    public Response<Account> openFeign(@PathVariable("id") String id) {
        log.info("请求id查询: id={}", id);
        return accountService.getById(id);
    }
}
