package com.tttiger.sql.constant;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 22:43
 */
public enum IdType {
    /**
     * 主键为自增
     */
    AUTO,
    /**
     * 主键为手动指定
     */
    INPUT,
    /**
     * 主键为主键生成器生成
     */
    GENERATOR
}
