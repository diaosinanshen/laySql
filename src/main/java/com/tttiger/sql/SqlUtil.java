package com.tttiger.sql;

import com.tttiger.sql.annotation.Table;
import com.tttiger.sql.annotation.TableField;
import com.tttiger.sql.annotation.TableId;
import com.tttiger.sql.annotation.TableLogicalField;
import com.tttiger.sql.exception.MissingNecessaryAnnotationException;
import com.tttiger.util.ReflectUtil;
import com.tttiger.util.StringUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 指定实体类，生成通用mysql语句
 *
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/08 23:43
 */
public class SqlUtil {

    /**
     * 获取属性对应的sql值的字符串
     *
     * @param field  属性
     * @param entity 实体
     * @return sql值字符串
     */
    public static Object getFieldSqlValue(Field field, Object entity) {
        try {
            Object value = ReflectUtil.getValueUseGetterMethod(entity, field.getName());
            return value;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 获取属性对应的数据库字段名
     *
     * @param field 属性
     * @return 数据库字段名
     */
    public static String getFieldSqlName(Field field) {
        if (field.isAnnotationPresent(TableId.class)
                && StringUtil.isNotEmpty(field.getAnnotation(TableId.class).value())) {
            return field.getAnnotation(TableId.class).value();
        } else if (field.isAnnotationPresent(TableField.class)
                && StringUtil.isNotEmpty(field.getAnnotation(TableField.class).value())) {
            return field.getAnnotation(TableField.class).value();
        } else {
            return StringUtil.humpToUnderline(field.getName());
        }
    }

    /**
     * 将属性转为mysql对应类型的字符
     *
     * @param fields 属性
     * @param entity 目标实体
     * @return 属性对应数据库字符集合
     */
    public static List<String> getFieldSqlValues(List<Field> fields, Object entity) {
        List<String> dbValue = new ArrayList<>();
        try {
            for (Field field : fields) {
                Object value = ReflectUtil.getValueUseGetterMethod(entity, field.getName());
                dbValue.add(SqlUtil.convertToDBValue(value));
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return dbValue;
    }

    public static List<String> getFieldSqlNames(Class<?> clazz) {
        List<String> names = new ArrayList<>();
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(TableField.class) && !field.getAnnotation(TableField.class).exist()) {
                continue;
            }
            names.add(StringUtil.humpToUnderline(field.getName()));
        }
        return names;
    }

    /**
     * java包装类型转为数据库对应类型
     *
     * @param value java类型
     * @return 数据库类型字符
     */
    public static String convertToDBValue(Object value) {
        if (value instanceof String) {
            return "'" + value + "'";
        }
        return value == null ? "null" : value.toString();
    }

    /**
     * 获取属性对应的表字段名，如果标注TableField注解，并且指定value那么使用自定义字段名
     * 否则使用下划线命名规则
     *
     * @param fields 获取字段名属性集合
     * @return 对应字段名集合
     */
    public static List<String> getTableField(List<Field> fields) {
        List<String> list = new ArrayList<>();
        for (Field field : fields) {
            if (field.isAnnotationPresent(TableField.class)
                    && StringUtil.isNotEmpty(field.getAnnotation(TableField.class).value())) {
                list.add(field.getAnnotation(TableField.class).value());
            } else {
                list.add(StringUtil.humpToUnderline(field.getName()));
            }
        }
        return list;
    }


    /**
     * 获取存在表字段映射的属性
     *
     * @param clazz 实体类型
     * @return 存在映射的非null可空集合
     */
    public static List<Field> getExistMappingField(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> fields = new ArrayList<>();
        for (Field field : declaredFields) {
            // 属性是tableId获取标识属性不存在映射
            if (field.isAnnotationPresent(TableId.class) ||
                    (field.isAnnotationPresent(TableField.class) && !field.getAnnotation(TableField.class).exist())) {
                continue;
            }
            fields.add(field);
        }
        return fields;
    }

    /**
     * 获取实体对应表名，如果未使用Table注解指定，默认为下划线命名表名
     *
     * @param clazz 实体类型
     * @return 表名
     */
    public static String getTableName(Class<?> clazz) {
        if (clazz.isAnnotationPresent(Table.class)) {
            return clazz.getAnnotation(Table.class).value();
        }
        return StringUtil.humpToUnderline(clazz.getSimpleName());
    }


    /**
     * 获取@TableId标注的属性
     *
     * @param clazz 目标类型
     * @return 标注了TableId注解的属性
     */
    public static Field getTableIdField(Class<?> clazz) {
        List<Field> idField = ReflectUtil.getFieldByAnnotation(clazz, TableId.class);
        if (idField.isEmpty()) {
            throw new MissingNecessaryAnnotationException("未指定@TableId注解");
        }
        return idField.get(0);
    }

    public static boolean hasLogical(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(TableLogicalField.class)) {
                return true;
            }
        }
        return false;
    }

    public static Field getLogicalField(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(TableLogicalField.class)) {
                return field;
            }
        }
        return null;
    }


    public static String getFieldAssignmentStr(Field field, Object entity) {
        Object value = getFieldSqlValue(field, entity);
        return getFieldSqlName(field) + " = " + value;
    }


    public static String getDBFieldStr(Class<?> clazz) {
        StringBuilder sql = new StringBuilder();
        List<Field> existMappingField = getExistMappingField(clazz);
        List<String> tableField = getTableField(existMappingField);
        for (String fieldName : tableField) {
            sql.append(fieldName).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        return sql.toString();
    }


    public static String getDBFieldStr(List<Field> fields) {
        StringBuilder sql = new StringBuilder();
        List<String> tableField = getTableField(fields);
        for (String fieldName : tableField) {
            sql.append(fieldName).append(",");
        }
        sql.deleteCharAt(sql.length() - 1);
        return sql.toString();
    }

    public static String getIdFieldStr(Class<?> clazz) {
        Field id = getTableIdField(clazz);
        TableId annotation = id.getAnnotation(TableId.class);
        String idFieldName = annotation.value();
        if (StringUtil.isEmpty(annotation.value())) {
            idFieldName = StringUtil.humpToUnderline(id.getName());
        }
        return " " + idFieldName + " ";
    }

    public static Map<String, Field> getSqlNameFieldMapping(Class<?> clazz) {
        List<Field> declaredFields = SqlUtil.getExistMappingField(clazz);
        Map<String, Field> map = new HashMap<>();
        for (Field field : declaredFields) {
            map.put(getFieldSqlName(field), field);
        }
        return map;
    }


}
