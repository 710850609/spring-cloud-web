package me.linbo.demo.web.api.consumer.service.others;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.demo.web.api.consumer.model.Account;
import me.linbo.demo.web.api.consumer.model.AccountQueryDto;
import org.springframework.stereotype.Service;

/**
 * @author LinBo
 * @date 2019-10-23 14:13
 */
@Service
@Slf4j
public class AccountServiceFallbackFactory implements FallbackFactory<AccountService> {

    @Override
    public AccountService create(Throwable throwable) {
        return new AccountService() {
            @Override
            public Response<PageDto<Account>> list(AccountQueryDto params) {
                log.error("AccountService list", throwable);
                return Response.error(throwable);
            }

            @Override
            public Response<Account> getById(String id) {
                log.error("AccountService getById", throwable);
                return Response.error(throwable);
            }
        };
    }

}