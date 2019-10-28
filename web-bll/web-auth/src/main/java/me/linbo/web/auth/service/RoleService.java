package me.linbo.web.auth.service;

import lombok.extern.slf4j.Slf4j;
import me.linbo.web.auth.model.Role;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 角色服务
 * @author LinBo
 * @date 2019-10-28 15:36
 */
@Slf4j
@Service
public class RoleService {

    public List<Role> getByUserId(Long id) {
        return null;
    }
}
