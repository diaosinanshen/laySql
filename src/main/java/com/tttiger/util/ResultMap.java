package com.tttiger.util;


/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2019/09/28 20:24
 */
public class ResultMap {

    /**
     * 统一失败响应状态码
     */
    public static final int FAILED = 0;
    /**
     * 统一成功响应状态码
     */
    public static final int SUCCESS = 1;
    /**
     * 响应状态
     */
    private Integer status;
    /**
     * 响应消息
     */
    private String message;
    /**
     * 响应数据
     */
    private Object data;

    public static ResultMap ok() {
        ResultMap resultMap = new ResultMap();
        resultMap.setStatus(ResultMap.SUCCESS);
        return resultMap;
    }

    public static ResultMap fail() {
        ResultMap resultMap = new ResultMap();
        resultMap.setStatus(ResultMap.FAILED);
        resultMap.setMessage("未知错误");
        return resultMap;
    }

    public static ResultMap predicate(boolean b) {
        return b ? ResultMap.ok() : ResultMap.fail();
    }

    public static ResultMap predicate(boolean b, String failMessage) {
        return b ? ResultMap.ok() : ResultMap.fail().message(failMessage);
    }

    public ResultMap data(Object data) {
        this.data = data;
        return this;
    }

    public ResultMap message(String message) {
        this.message = message;
        return this;
    }

    /**
     * 如果 == null 返回失败响应，否则将obj设置为data返回
     *
     * @param obj data
     * @return 同一结果
     */
    public static ResultMap notNull(Object obj) {
        if (obj == null) {
            return ResultMap.fail().message("未找到匹配数据");
        }
        return ResultMap.ok().data(obj);
    }

    /**
     * 如果 == null 返回失败响应，否则将obj设置为data返回，并设置错误响应消息
     *
     * @param obj data
     * @return 同一结果
     */
    public static ResultMap notNull(Object obj, String ifNullMessage) {
        if (obj == null) {
            return ResultMap.fail().message(ifNullMessage);
        }
        return ResultMap.ok().data(obj);
    }


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
