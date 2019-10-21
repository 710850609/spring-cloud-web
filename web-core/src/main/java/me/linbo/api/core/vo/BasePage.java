package me.linbo.api.core.vo;

import lombok.Data;

/**
 * @author LinBo
 * @date 2019-10-17 14:13
 */
@Data
public class BasePage {

    private Long pageNo = 1L;

    private Long pageSize = 10L;

}
