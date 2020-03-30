package com.tttiger.sql.mapper;

import com.tttiger.sql.*;
import com.tttiger.sql.exception.MoreResultException;
import com.tttiger.sql.executor.DefaultSqlExecutor;
import com.tttiger.sql.executor.SqlExecutor;
import com.tttiger.sql.handler.TestDataSourceSupplier;
import com.tttiger.sql.wrapper.Wrapper;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 13:59
 */
public abstract class BaseMapper<T> implements Mapper<T> {

    /**
     * Mapper处理实体对应的配置
     */
    private MapperConfiguration configuration;

    /**
     * sql执行器
     */
    private SqlExecutor<T> executor;

    @Override
    public List<T> select(Wrapper wrapper) {
        SqlMethod sqlMethod = SqlBuilder.buildSelect(wrapper, configuration);
        Result execute = executor.execute(sqlMethod);
        return (List<T>) execute.getResult();
    }

    @Override
    public T selectById(Serializable id) {
        SqlMethod sqlMethod = SqlBuilder.buildSelectById(id, configuration);
        Result execute = executor.execute(sqlMethod);
        List resultList = (List) execute.getResult();
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        if (resultList.size() > 1) {
            throw new MoreResultException("返回过多的结果");
        }
        return (T) resultList.get(0);
    }

    @Override
    public boolean insert(T t) {
        SqlMethod sqlMethod = SqlBuilder.buildInsert(t, configuration);
        Result execute = executor.execute(sqlMethod);
        return ((int) execute.getResult()) == 1;
    }

    @Override
    public boolean updateById(T t) {
        SqlMethod sqlMethod = SqlBuilder.buildUpdateById(t, configuration);
        Result execute = executor.execute(sqlMethod);
        return ((int) execute.getResult()) == 1;
    }

    @Override
    public int update(T t, Wrapper wrapper) {
        SqlMethod sqlMethod = SqlBuilder.buildUpate(t, wrapper, configuration);
        Result execute = executor.execute(sqlMethod);
        return (int) execute.getResult();
    }

    @Override
    public boolean deleteById(Serializable id) {
        SqlMethod sqlMethod = SqlBuilder.buildDeleteById(id, configuration);
        Result execute = executor.execute(sqlMethod);
        return (int) execute.getResult() == 1;
    }

    @Override
    public int delete(Wrapper wrapper) {
        SqlMethod sqlMethod = SqlBuilder.buildDelete(wrapper, configuration);
        Result execute = executor.execute(sqlMethod);
        return (int) execute.getResult();
    }


    public BaseMapper() {
        Class<?> genericsType = getGenericsType();
        this.configuration = new MapperConfiguration(genericsType);
        this.executor = new DefaultSqlExecutor<>(new TestDataSourceSupplier(), genericsType);
    }

    /**
     * 获取父类泛型
     */
    private Class<?> getGenericsType() {
        ParameterizedType pt = (ParameterizedType) getClass().getGenericSuperclass();
        return (Class) pt.getActualTypeArguments()[0];
    }

    /**
     * 指定语句执行器
     */
    @Override
    public SqlExecutor<T> getExecutor() {
        return null;
    }

}
