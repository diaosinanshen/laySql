package com.tttiger.sql.handler;

import com.tttiger.sql.Result;
import com.tttiger.sql.constant.SqlType;
import com.tttiger.sql.SqlUtil;
import com.tttiger.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/11 19:00
 */
public class DefaultResultHandler<T> extends AbstractResultHandler<T> {


    private Map<String, Field> sqlNameFieldMapping;


    public DefaultResultHandler(Class<?> handleType) {
        super(handleType);
        this.sqlNameFieldMapping = SqlUtil.getSqlNameFieldMapping(handleType);
    }


    @Override
    public Result handleListResult(PreparedStatement preparedStatement, SqlType sqlType) {

        Result result = new Result();
        try {
            if (sqlType == SqlType.SELECT) {
                List<T> list = new ArrayList<>();
                ResultSet resultSet = preparedStatement.getResultSet();
                Set<Map.Entry<String, Field>> entries = sqlNameFieldMapping.entrySet();
                ReflectUtil.getInstance(handleType);
                while (resultSet.next()) {
                    T t = buildObject();
                    for (Map.Entry<String, Field> entry : entries) {
                        int column = resultSet.findColumn(entry.getKey());
                        setAttribute(entry.getValue(), t, resultSet.getObject(column));
                    }
                    list.add(t);
                }
                result.setResult(list);
            } else {

                result.setResult(preparedStatement.getUpdateCount());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }


    private void setAttribute(Field field, Object obj, Object value) {
        try {
            Class<?> type = field.getType();
            if (type.equals(Integer.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Integer.parseInt(value.toString()), field.getName());
            } else if (type.equals(Double.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Double.parseDouble(value.toString()), field.getName());
            } else if (type.equals(String.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, value.toString(), field.getName());
            } else if (type.equals(Boolean.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Boolean.parseBoolean(value.toString()), field.getName());
            } else if (type.equals(Long.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Long.parseLong(value.toString()), field.getName());
            } else if (type.equals(Byte.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Byte.parseByte(value.toString()), field.getName());
            } else if (type.equals(Short.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Short.parseShort(value.toString()), field.getName());
            } else if (type.equals(Float.class)) {
                ReflectUtil.setValueUseSetterMethod(obj, Float.parseFloat(value.toString()), field.getName());
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private T buildObject() {
        Object instance = ReflectUtil.getInstance(handleType);
        return (T) instance;
    }
}
