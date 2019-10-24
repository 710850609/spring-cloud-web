package me.linbo.demo.web.consumer.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.linbo.api.core.vo.BasePage;

/**
 * @author LinBo
 * @date 2019-10-18 13:36
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AccountQueryDto extends BasePage {

    private Long id;

    private String name;

    private Integer gender;

}
