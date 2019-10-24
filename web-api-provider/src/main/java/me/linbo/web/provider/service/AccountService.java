package me.linbo.web.provider.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.linbo.api.core.vo.PageDto;
import me.linbo.web.provider.model.domain.Account;
import me.linbo.web.provider.model.dto.AccountQueryDto;

/**
 * @author LinBo
 * @date 2019-10-17 11:44
 */
public interface AccountService extends IService<Account> {

    /**
     * 分页查询
     * @Author LinBo
     * @Date 2019-10-18 13:32
     * @param params
     * @return {@link Page< Account>}
     **/
    PageDto<Account> list(AccountQueryDto params);

}
