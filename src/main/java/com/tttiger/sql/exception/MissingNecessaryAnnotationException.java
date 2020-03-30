package com.tttiger.sql.exception;

/**
 * 缺少必须注解
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/09 15:02
 */
public class MissingNecessaryAnnotationException extends RuntimeException {
    public MissingNecessaryAnnotationException(String message) {
        super(message);
    }
}
