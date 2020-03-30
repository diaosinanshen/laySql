package com.tttiger.sql.mapper;

import com.tttiger.sql.wrapper.Wrapper;
import com.tttiger.sql.executor.SqlExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 13:59
 */
public interface Mapper<T> {

    /**
     * 根据条件查询
     *
     * @param wrapper 查询条件
     * @return 查询结果
     */
    List<T> select(Wrapper wrapper);

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return 查询结果
     */
    T selectById(Serializable id);

    /**
     * 数据插入
     *
     * @param t 数据实体
     * @return 是否成功
     */
    boolean insert(T t);

    /**
     * 根据主键更新实体非空字段
     *
     * @param t 包含主键实体
     * @return 是否成
     */
    boolean updateById(T t);

    /**
     * 根据条件更新，实体非空字段
     *
     * @param t       实体
     * @param wrapper 条件
     * @return 影响行数
     */
    int update(T t, Wrapper wrapper);

    /**
     * 进行真实删除或逻辑删除
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Serializable id);

    /**
     * 根据条件进行真实删除或逻辑删除
     *
     * @param wrapper 条件封装
     * @return 影响行数
     */
    int delete(Wrapper wrapper);


    /**
     * 返回sql执行器
     *
     * @return sql执行器
     */
    SqlExecutor<T> getExecutor();
}
