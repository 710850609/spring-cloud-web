package me.linbo.web.auth.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 访问证件
 * @author LinBo
 * @date 2019-10-28 15:27
 */
@Data
public class AccessCredentials implements Serializable {

    private String userName;

}
