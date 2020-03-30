package com.tttiger.sql;

import java.lang.reflect.Field;
import java.util.List;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/13 22:47
 */
public class MapperConfiguration {
    /**
     * 处理类型
     */
    private Class<?> clazz;
    /**
     * 实体对应表名
     */
    private String tableName;
    /**
     * 拼接好的表字段属性
     */
    private String fieldsName;
    /**
     * 存在表字段映射的属性
     */
    private List<Field> fields;
    /**
     * 实体主键属性对应的表字段名
     */
    private String idName;
    /**
     * 实体主键属性
     */
    private Field idField;
    /**
     * 实体逻辑删除属性对应表字段名
     */
    private String logicalName;
    /**
     * 实体逻辑删除属性
     */
    private Field logical;



    public boolean hasLogical(){
        return logical != null;
    }


    public MapperConfiguration(Class<?> entityType) {
        this.clazz = entityType;
        init();
    }

    private void init() {
        this.tableName = SqlUtil.getTableName(clazz);
        this.fieldsName = SqlUtil.getDBFieldStr(clazz);
        this.idName = SqlUtil.getIdFieldStr(clazz);
        this.fields = SqlUtil.getExistMappingField(clazz);
        this.idField = SqlUtil.getTableIdField(clazz);
        this.logical = SqlUtil.getLogicalField(clazz);
        if (logical != null) {
            this.logicalName = SqlUtil.getFieldSqlName(logical);
        }
    }


    public Class<?> getClazz() {
        return clazz;
    }

    public String getTableName() {
        return tableName;
    }

    public String getFieldsName() {
        return fieldsName;
    }

    public List<Field> getFields() {
        return fields;
    }

    public String getIdName() {
        return idName;
    }

    public Field getIdField() {
        return idField;
    }

    public String getLogicalName() {
        return logicalName;
    }

    public Field getLogical() {
        return logical;
    }
}
