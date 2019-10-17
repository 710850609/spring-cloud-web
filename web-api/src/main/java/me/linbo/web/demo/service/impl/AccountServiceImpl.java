package me.linbo.web.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import me.linbo.web.demo.mapper.AccountMapper;
import me.linbo.web.demo.model.domain.Account;
import me.linbo.web.demo.service.AccountService;
import org.springframework.stereotype.Service;

/**
 * @author LinBo
 * @date 2019-10-17 11:54
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
