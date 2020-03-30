package com.tttiger.sql.executor;

import com.tttiger.sql.Result;
import com.tttiger.sql.SqlMethod;
import com.tttiger.sql.handler.DataSourceSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/10 17:11
 */
public class DefaultSqlExecutor<T> extends AbstractSqlExecutor<T> {

    public DefaultSqlExecutor(DataSourceSupplier dataSourceSupplier, Class<?> executeType) {
        super(dataSourceSupplier,executeType);
    }

    @Override
    public Result execute(SqlMethod sqlMethod) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = dataSourceSupplier.getConnection();
            preparedStatement = connection.prepareStatement(sqlMethod.getSql());
            preparedStatement.execute();
            return resultHandler.handleListResult(preparedStatement,sqlMethod.getSqlType());
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
