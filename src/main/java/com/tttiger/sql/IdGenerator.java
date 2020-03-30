package com.tttiger.sql;

/**
 * 主键id生成器
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 22:47
 */
public interface IdGenerator<T> {

    /**
     *
     */
    T getId();
}
