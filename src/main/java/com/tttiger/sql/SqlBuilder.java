package com.tttiger.sql;

import com.tttiger.sql.annotation.TableId;
import com.tttiger.sql.constant.IdType;
import com.tttiger.sql.constant.Logical;
import com.tttiger.sql.wrapper.Wrapper;
import com.tttiger.util.StringUtil;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/13 22:55
 */
public class SqlBuilder {

    /**
     * id生成器
     */
    private static IdGenerator<String> idGenerator = new UuidGenerator();

    /**
     * 根据条件查询
     *
     * @param wrapper 查询条件
     * @return 查询结果
     */
    public static SqlMethod buildSelect(Wrapper wrapper, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder(SqlWord.SELECT);
        sql.append(configuration.getFieldsName());
        sql.append(SqlWord.FROM).append(configuration.getTableName());
        setWrapper(sql, wrapper);
        setLogicalCondition(sql, wrapper, configuration);
        return new SqlMethod(sql.toString());
    }

    /**
     * 根据主键进行查询
     *
     * @param id 主键
     * @return 查询结果
     */
    public static SqlMethod buildSelectById(Serializable id, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder(SqlWord.SELECT);
        sql.append(configuration.getFieldsName());
        sql.append(SqlWord.FROM).append(configuration.getTableName()).append(SqlWord.WHERE)
                .append(configuration.getIdName()).append(SqlWord.EQ).append(SqlUtil.convertToDBValue(id));
        return new SqlMethod(sql.toString());
    }

    /**
     * 数据插入
     *
     * @param t 数据实体
     * @return 是否成功
     */
    public static SqlMethod buildInsert(Object t, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder(SqlWord.INSERT);
        sql.append(SqlWord.INTO);
        // 添加表名
        sql.append(configuration.getTableName());
        // 获取存在表字段映射的属性
        StringBuilder fieldStr = new StringBuilder();
        StringBuilder valueStr = new StringBuilder();
        fieldStr.append("(");
        valueStr.append(" VALUES(");

        IdType idType = configuration.getIdField().getAnnotation(TableId.class).ID_TYPE();
        if (idType == IdType.INPUT) {
            fieldStr.append(configuration.getIdName()).append(",");
            valueStr.append(SqlUtil.getFieldSqlValue(configuration.getIdField(), t)).append(",");
        } else if (idType == IdType.GENERATOR) {
            fieldStr.append(configuration.getIdName()).append(",");
            valueStr.append(idGenerator.getId()).append(",");
        }

        for (Field temp : configuration.getFields()) {
            fieldStr.append(StringUtil.humpToUnderline(temp.getName())).append(",");
            valueStr.append(SqlUtil.convertToDBValue(SqlUtil.getFieldSqlValue(temp, t))).append(",");
        }
        fieldStr.deleteCharAt(fieldStr.length() - 1);
        valueStr.deleteCharAt(valueStr.length() - 1);
        fieldStr.append(")");
        valueStr.append(")");
        sql.append(fieldStr.append(valueStr));
        return new SqlMethod(sql.toString());
    }

    /**
     * 根据主键更新实体非空字段
     *
     * @param t 包含主键实体
     * @return 是否成
     */
    public static SqlMethod buildUpdateById(Object t, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder(SqlWord.UPDATE);
        sql.append(configuration.getTableName()).append(SqlWord.SET);
        for (Field temp : configuration.getFields()) {
            Object fieldValue = SqlUtil.getFieldSqlValue(temp, t);
            if (fieldValue == null || temp.isAnnotationPresent(TableId.class)) {
                continue;
            }
            sql.append(SqlUtil.getFieldSqlName(temp))
                    .append(SqlWord.EQ).append(SqlUtil.convertToDBValue(fieldValue)).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        sql.append(SqlWord.WHERE).append(SqlUtil.getFieldAssignmentStr(configuration.getIdField(), t));
        return new SqlMethod(sql.toString());
    }

    /**
     * 根据条件更新，实体非空字段
     *
     * @param t       实体
     * @param wrapper 条件
     * @return 影响行数
     */
    public static SqlMethod buildUpate(Object t, Wrapper wrapper, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder(SqlWord.UPDATE);
        sql.append(configuration.getTableName()).append(SqlWord.SET);
        for (Field temp : configuration.getFields()) {
            Object fieldValue = SqlUtil.getFieldSqlValue(temp, t);
            if (fieldValue == null || temp.isAnnotationPresent(TableId.class)) {
                continue;
            }
            sql.append(SqlUtil.getFieldSqlName(temp))
                    .append(SqlWord.EQ).append(SqlUtil.convertToDBValue(fieldValue)).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        setWrapper(sql, wrapper);
        return new SqlMethod(sql.toString());
    }

    /**
     * 进行真实删除或逻辑删除
     *
     * @param id 主键
     * @return 是否成功
     */
    public static SqlMethod buildDeleteById(Serializable id, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder("");
        Field logical = configuration.getLogical();
        String tableName = configuration.getTableName();
        String idName = configuration.getIdName();
        // 存在逻辑字段使用逻辑删除
        if (logical == null) {
            sql.append(SqlWord.DELETE).append(SqlWord.FROM)
                    .append(tableName).append(SqlWord.WHERE).append(idName).append(SqlWord.EQ).append(SqlUtil.convertToDBValue(id));
        } else {
            sql.append(SqlWord.UPDATE).append(tableName).append(SqlWord.SET);
            sql.append(configuration.getLogicalName()).append(SqlWord.EQ).append(Logical.DELETED);
            sql.append(SqlWord.WHERE).append(idName).append(SqlWord.EQ).append(SqlUtil.convertToDBValue(id));
        }
        return new SqlMethod(sql.toString());
    }

    /**
     * 根据条件进行真实删除或逻辑删除
     *
     * @param wrapper 条件封装
     * @return 影响行数
     */
    public static SqlMethod buildDelete(Wrapper wrapper, MapperConfiguration configuration) {
        StringBuilder sql = new StringBuilder();
        // 存在逻辑字段使用逻辑删除
        Field logical = configuration.getLogical();
        String tableName = configuration.getTableName();
        String logicalName = configuration.getLogicalName();
        if (logical == null) {
            sql.append(SqlWord.DELETE).append(SqlWord.FROM)
                    .append(tableName).append(SqlWord.WHERE).append(wrapper.getConditionSql());
        } else {
            sql.append(SqlWord.UPDATE).append(tableName).append(SqlWord.SET);
            sql.append(logicalName).append(SqlWord.EQ).append(Logical.DELETED);
            sql.append(SqlWord.WHERE).append(wrapper.getConditionSql());
        }
        return null;
    }

    private static void setWrapper(StringBuilder sql, Wrapper wrapper) {
        if (wrapper != null) {
            sql.append(SqlWord.WHERE);
            sql.append(wrapper.getConditionSql());
        }
    }

    private static void setLogicalCondition(StringBuilder sql, Wrapper wrapper, MapperConfiguration configuration) {
        if (configuration.getLogical() != null && wrapper != null && StringUtil.isNotEmpty(wrapper.getConditionSql())) {
            sql.append(SqlWord.AND).append(configuration.getLogicalName()).append(SqlWord.EQ).append(Logical.EXIST);
        } else if (configuration.getLogical() != null && (wrapper == null || StringUtil.isEmpty(wrapper.getConditionSql()))) {
            sql.append(SqlWord.WHERE).append(configuration.getLogicalName()).append(SqlWord.EQ).append(Logical.EXIST);
        }
    }
}
