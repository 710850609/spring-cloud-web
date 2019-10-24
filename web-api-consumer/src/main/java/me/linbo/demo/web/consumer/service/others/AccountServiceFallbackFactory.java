package me.linbo.demo.web.consumer.service.others;

import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.demo.web.consumer.model.Account;
import me.linbo.demo.web.consumer.model.AccountQueryDto;
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

            @Override
            public Response<Account> add(Account params) {
                log.error("AccountService add", throwable);
                return Response.error(throwable);
            }

            @Override
            public Response update(Long id, Account params) {
                log.error("AccountService update", throwable);
                return Response.error(throwable);
            }

            @Override
            public Response delete(Long id) {
                log.error("AccountService delete", throwable);
                return Response.error(throwable);
            }
        };
    }

}