package com.tttiger.sql.handler;

import com.tttiger.sql.Result;
import com.tttiger.sql.constant.SqlType;

import java.sql.PreparedStatement;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 18:59
 */
public interface ResultHandler<T> {

    /**
     * 处理返回多条数据查询结果
     * @param preparedStatement 执行完成后的preparedStatement
     * @return 封装好的结果
     */
    Result handleListResult(PreparedStatement preparedStatement, SqlType sqlType);

}
