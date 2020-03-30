package com.tttiger.sql;

import com.tttiger.sql.constant.SqlType;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 20:43
 */
public class SqlMethod {

    /**
     * 执行的sql语句
     */
    private String sql;
    /**
     * sql 类型
     */
    private SqlType sqlType;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }


    public SqlMethod(String sql) {
        this.sql = sql;
        pareseSqlType(sql);
    }

    public SqlType getSqlType() {
        return sqlType;
    }

    private void pareseSqlType(String sql) {
        String str = sql.trim().toUpperCase();
        str = str.substring(0,6);
        switch (str) {
            case "SELECT":
                this.sqlType = SqlType.SELECT;
                break;
            case "DELETE":
                this.sqlType = SqlType.DELETE;
                break;
            case "UPDATE":
                this.sqlType = SqlType.UPDATE;
                break;
            case "INSERT":
                this.sqlType = SqlType.INSERT;
                break;
            default:
                throw new RuntimeException();
        }
    }
}
