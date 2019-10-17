package me.linbo.api.core.exception;

import lombok.Getter;

/**
 * @author LinBo
 * @date 2019-10-17 14:40
 */
@Getter
public class BaseException extends RuntimeException {

    private Integer code;

    private String msg;

    public BaseException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
