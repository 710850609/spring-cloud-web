package me.linbo.web.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.PageDto;
import me.linbo.api.core.vo.Response;
import me.linbo.web.demo.model.domain.Account;
import me.linbo.web.demo.model.dto.AccountQueryDto;
import me.linbo.web.demo.service.AccountService;
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

    @PostMapping("")
    public Response<Account> add(@RequestBody Account params) {
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
        params.setId(id);
        accountService.updateById(params);
        return Response.ok();
    }

    @DeleteMapping("/{id}")
    public Response delete(@PathVariable("id")Long id) {
        accountService.removeById(id);
        return Response.ok();
    }
}
