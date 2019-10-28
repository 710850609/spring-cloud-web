package me.linbo.web.auth.service;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.auth.model.Account;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 认证服务
 * @author LinBo
 * @date 2019-10-28 15:35
 */
@Slf4j
@Service
public class AccountService implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = new Account();
        account.setUserName(username);
        account.setPassword("123");
        return account;
    }
}
