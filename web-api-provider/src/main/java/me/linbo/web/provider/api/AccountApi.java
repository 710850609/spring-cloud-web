package me.linbo.web.provider.api;

import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.web.provider.model.domain.Account;
import me.linbo.web.provider.model.dto.AccountQueryDto;
import me.linbo.web.provider.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author LinBo
 * @date 2019-10-17 11:42
 */
@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountApi {

    @Autowired
    private AccountService accountService;

    @GetMapping("")
    public Response<PageDto<Account>> list(AccountQueryDto params) {
        log.info("请求查询列表:{}", params);
        PageDto<Account> page = accountService.list(params);
        return Response.ok(page);
    }

    @GetMapping("/{id}")
    public Response<Account> getById(@PathVariable("id")String id) {
        log.info("请求查询id: id={}", id);
        Account account = accountService.getById(id);
        return Response.ok(account);
    }

    @PostMapping("")
    public Response<Account> add(@RequestBody Account params) {
        log.info("请求新增:{}", params);
        if (params.getPwd() == null) {
            params.setPwd("123456");
        }
        if (params.getSalt() == null) {
            params.setSalt("123456");
        }
        accountService.save(params);
        return Response.ok(params);
    }

    @PutMapping("/{id}")
    public Response update(@PathVariable("id") Long id, @RequestBody Account params) {
        log.info("请求更新: id={}, params={}", id, params);
        params.setId(id);
        accountService.updateById(params);
        return Response.ok();
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable("id")Long id) {
        log.info("请求删除: id={}", id);
        accountService.removeById(id);
        return Response.ok();
    }
}
