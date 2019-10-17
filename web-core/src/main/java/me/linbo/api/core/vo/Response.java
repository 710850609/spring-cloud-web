package me.linbo.api.core.vo;

import lombok.Data;
import me.linbo.api.core.exception.BaseException;

import java.io.Serializable;

/**
 * 接口返回类型
 * @author LinBo
 * @date 2019-10-17 14:09
 */
@Data
public class Response<T> implements Serializable {

    /** 成功编码 */
    public static final Integer CODE_SUCCESS = 1;
    /** 系统异常编码 */
    public static final Integer CODE_SYSTEM_ERROR = 0;

    private Integer code;

    private String msg;

    private T data;

    public static <T> Response<T> build(Integer code, String msg, T obj) {
        Response<T> response = new Response<>();
        response.setCode(code);
        response.setMsg(msg);
        response.setData(obj);
        return response;
    }

    public static <T> Response<T> ok(T obj) {
        return build(CODE_SUCCESS, null, obj);
    }

    public static <T> Response<T> error(BaseException e) {
        Response<T> response = new Response<>();
        response.setCode(e.getCode());
        response.setMsg(e.getMsg());
        return response;
    }

    public static <T> Response<T> error(Exception e) {
        return build(CODE_SYSTEM_ERROR, "未知的异常" + e.getMessage(), null);
    }
}
