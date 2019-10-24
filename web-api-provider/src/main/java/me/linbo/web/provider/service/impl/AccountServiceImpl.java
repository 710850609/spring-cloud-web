package me.linbo.web.provider.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.linbo.api.core.vo.PageDto;
import me.linbo.web.provider.mapper.AccountMapper;
import me.linbo.web.provider.model.domain.Account;
import me.linbo.web.provider.model.dto.AccountQueryDto;
import me.linbo.web.provider.service.AccountService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author LinBo
 * @date 2019-10-17 11:54
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public PageDto<Account> list(AccountQueryDto params) {
        Page<Account> page = new Page<>(params.getPageNo(), params.getPageSize());
        List<Account> list = baseMapper.list(page, params);
        page.setRecords(list);
        return PageDto.build(page);
    }

}
