package me.linbo.demo.web.api.consumer.service.others;

import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.demo.web.api.consumer.config.FeignClientConfig;
import me.linbo.demo.web.api.consumer.model.Account;
import me.linbo.demo.web.api.consumer.model.AccountQueryDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author LinBo
 * @date 2019-10-23 13:05
 */
@FeignClient(name = "web-api", path = "/accounts", configuration = FeignClientConfig.class, fallbackFactory = AccountServiceFallbackFactory.class)
public interface AccountService {

    @GetMapping("")
//    Response<PageDto<Account>> list(@RequestParam AccountQueryDto params);
    Response<PageDto<Account>> list(@SpringQueryMap AccountQueryDto params);

    @GetMapping("/{id}")
    Response<Account> getById(@PathVariable("id") String id);
}
