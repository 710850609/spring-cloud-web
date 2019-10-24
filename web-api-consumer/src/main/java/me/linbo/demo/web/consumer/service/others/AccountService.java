package me.linbo.demo.web.consumer.service.others;

import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.demo.web.consumer.config.FeignClientConfig;
import me.linbo.demo.web.consumer.model.Account;
import me.linbo.demo.web.consumer.model.AccountQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * @author LinBo
 * @date 2019-10-23 13:05
 */
@FeignClient(name = "web-api-provider", path = "/accounts", configuration = FeignClientConfig.class, fallbackFactory = AccountServiceFallbackFactory.class)
public interface AccountService {

    @GetMapping("")
    Response<PageDto<Account>> list(@SpringQueryMap AccountQueryDto params);

    @GetMapping("/{id}")
    Response<Account> getById(@PathVariable("id") String id);

    @PostMapping("")
    Response<Account> add(@RequestBody Account params);

    @PutMapping("/{id}")
    Response update(@PathVariable("id") Long id, @RequestBody Account params);

    @DeleteMapping("/{id}")
    Response delete(@PathVariable("id")Long id);
}
