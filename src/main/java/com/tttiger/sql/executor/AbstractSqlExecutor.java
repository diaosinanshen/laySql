package com.tttiger.sql.executor;

import com.tttiger.sql.handler.DataSourceSupplier;
import com.tttiger.sql.handler.DefaultResultHandler;
import com.tttiger.sql.handler.ResultHandler;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/12 21:31
 */
public abstract class AbstractSqlExecutor<T> implements SqlExecutor<T> {

    protected DataSourceSupplier dataSourceSupplier;

    protected Class<?> executeType;

    protected ResultHandler<T> resultHandler;

    public AbstractSqlExecutor(DataSourceSupplier dataSourceSupplier, Class<?> executeType) {
        this.dataSourceSupplier = dataSourceSupplier;
        this.executeType = executeType;
        this.resultHandler = new DefaultResultHandler<>(executeType);
    }
}
