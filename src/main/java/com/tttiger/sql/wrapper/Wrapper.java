package com.tttiger.sql.wrapper;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 9:54
 */
public interface Wrapper {

    /**
     * 返回封装好的条件
     * @return SQL
     */
    String getConditionSql();
}
