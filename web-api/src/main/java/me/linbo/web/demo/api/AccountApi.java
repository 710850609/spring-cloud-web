package me.linbo.web.demo.api;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import me.linbo.api.core.vo.QueryDTO;
import me.linbo.api.core.vo.Response;
import me.linbo.web.demo.model.domain.Account;
import me.linbo.web.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<IPage<Account>> list(QueryDTO params) {
        log.info("请求查询列表:{}", params);
        Page<Account> page = new Page<>(params.getPageNo(), params.getPageSize());
        page = (Page<Account>) accountService.page(page);
        return Response.ok(page);
    }
}
