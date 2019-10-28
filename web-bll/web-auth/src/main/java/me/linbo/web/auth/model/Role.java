package me.linbo.web.auth.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色, 相当于资源的一种集合
 * @author LinBo
 * @date 2019-10-28 15:37
 */
@Data
public class Role implements Serializable {

    private Long id;

    private String name;

    private List<Long> resources;
}
