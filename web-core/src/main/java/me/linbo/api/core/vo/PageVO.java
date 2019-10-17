package me.linbo.api.core.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author LinBo
 * @date 2019-10-17 14:04
 */
@Data
public class PageVO<T> implements Serializable {

    private Integer pageNo;

    private Integer pageSize;

    private List<T> data;

}
