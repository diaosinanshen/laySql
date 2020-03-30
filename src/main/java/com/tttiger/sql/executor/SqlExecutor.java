package com.tttiger.sql.executor;

import com.tttiger.sql.Result;
import com.tttiger.sql.SqlMethod;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 16:51
 */
public interface SqlExecutor<T> {

    /**
     * 执行sql方法返回处理结果
     * @param sqlMethod sql方法封装
     * @return 处理结果
     */
    Result execute(SqlMethod sqlMethod);
}
