package me.linbo.demo.web.consumer.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author LinBo
 * @date 2019-10-17 10:55
 */
@Data
public class Account implements Serializable {

    private Long id;

    private String name;

    private String pwd;

    private String salt;

    private Integer gender;

    private String mobile;

    private Integer state;

    private String email;

    private String idCardNo;

    private Date createTime;

    private Date updateTime;

    private String uid;

    private Date lastLoginTime;

    private String lastLoginIp;

    private Integer errorLoginCount;

    private Integer loginCount;

}
