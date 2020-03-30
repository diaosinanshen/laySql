package com.tttiger.sql.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/08 23:46
 */

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableField {
    /**
     * 自定义指定属性对应表字段
     */
    String value() default "";

    /**
     * 指定属性在表中是否存在对应字段，默认为true
     */
    boolean exist() default true;
}
