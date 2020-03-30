package com.tttiger.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表逻辑删除字段
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/08 23:47
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableLogicalField {

}
