package com.tttiger.sql.handler;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 19:14
 */
public abstract class AbstractResultHandler<T> implements ResultHandler {

    protected Class<?> handleType;

    public AbstractResultHandler(Class<?> handleType) {
        this.handleType = handleType;
    }
}
