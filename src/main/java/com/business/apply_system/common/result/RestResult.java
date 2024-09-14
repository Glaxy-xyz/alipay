package com.business.apply_system.common.result;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * 统一API响应结果封装
 * @author zjt
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestResult<T> {

    private static final String DEFAULT_SUCCESS_CODE = "200";
    private static final String DEFAULT_FAILED_CODE = "500";
    /*电子地图文件自动删除码*/
    private static final String MAP_FAILED_CODE = "501";
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    private static final String DEFAULT_FAILED_MESSAGE = "FAILED";

    private String code;
    private String message;
    private T data;

    public static <T> RestResult<T> getSuccessResult(T data) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setCode(DEFAULT_SUCCESS_CODE);
        restResult.setMessage(DEFAULT_SUCCESS_MESSAGE);
        restResult.setData(data);
        return restResult;
    }

    public static <T> RestResult<T> getFailResult(T data) {
        RestResult<T> restResult = new RestResult<>();
        restResult.setCode(DEFAULT_FAILED_CODE);
        restResult.setMessage(DEFAULT_FAILED_MESSAGE);
        restResult.setData(data);
        return restResult;
    }
}
