package me.linbo.api.core.vo;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinBo
 * @date 2019-10-17 14:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class PageDto<T> extends BasePage implements Serializable {

    private Long total;

    private List<T> records;

    public static <T> PageDto<T> build(IPage<T> page) {
        PageDto<T> dto = new PageDto<>();
        dto.setRecords(page.getRecords());
        dto.setPageNo(page.getCurrent());
        dto.setPageSize(page.getSize());
        dto.setTotal(page.getTotal());
        return dto;
    }
}
