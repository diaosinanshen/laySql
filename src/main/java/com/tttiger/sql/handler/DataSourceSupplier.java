package com.tttiger.sql.handler;

import java.sql.Connection;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/12 21:21
 */
public interface DataSourceSupplier {
    /**
     * 获取数据库连接
     * @return 数据库连接
     */
    Connection getConnection();
}
