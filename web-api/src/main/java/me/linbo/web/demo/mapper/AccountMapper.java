package me.linbo.web.demo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.linbo.web.demo.model.domain.Account;
import me.linbo.web.demo.model.dto.AccountQueryDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author LinBo
 * @date 2019-10-17 10:58
 */
public interface AccountMapper extends BaseMapper<Account> {

    /**
     * 查询列表
     * @Author LinBo
     * @Date 2019-10-18 15:07
     * @param page
     * @param params
     * @return {@link List< Account>}
     **/
    List<Account> list(Page page, @Param("params") AccountQueryDto params);
}
