package me.linbo.web.demo.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.linbo.api.core.vo.PageDto;
import me.linbo.web.demo.mapper.AccountMapper;
import me.linbo.web.demo.model.domain.Account;
import me.linbo.web.demo.model.dto.AccountQueryDto;
import me.linbo.web.demo.service.AccountService;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author LinBo
 * @date 2019-10-17 11:54
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Autowired
    private DataSource dataSource;

    @Override
    public PageDto<Account> list(AccountQueryDto params) {
        Page<Account> page = new Page<>(params.getPageNo(), params.getPageSize());
        List<Account> list = baseMapper.list(page, params);
        page.setRecords(list);
        return PageDto.build(page);
    }
}
