package com.tttiger.sql;

import com.tttiger.sql.mapper.BaseMapper;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * @author 秦浩桐
 * @version 1.0
 * @date 2020/01/14 21:00
 */
public class MapperProxyBeanPostProcessor implements BeanPostProcessor {


    @Override
    public Object postProcessBeforeInitialization(Object o, String beanName) throws BeansException {
        return null;
    }

    @Override
    public Object postProcessAfterInitialization(Object o, String beanName) throws BeansException {
        if(o instanceof BaseMapper){

        }
        return o;
    }
}
