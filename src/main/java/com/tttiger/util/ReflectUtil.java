package com.tttiger.util;


import javax.annotation.Nonnull;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 秦浩桐
 * @date 2019/12/24 03:28
 */
public class ReflectUtil {

    /**
     * 通过调用对象的getter方法来获取属性
     *
     * @param obj       指定对象
     * @param fieldName 指定属性名
     * @return 对象属性内容
     */
    public static Object getValueUseGetterMethod(Object obj, String fieldName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method declaredMethod = obj.getClass()
                .getDeclaredMethod("get" + StringUtil.toUpperCaseFirstOne(fieldName), null);
        return declaredMethod.invoke(obj, null);
    }

    /**
     * 通过调用对象的setter方法来尝试设置属性值
     *
     * @param obj       指定对象
     * @param value     设置属性
     * @param fieldName 指定属性名
     * @return 是否设置成功（异常已经捕获）
     */
    public static <T> boolean setValueUseSetterMethod(Object obj, T value, String fieldName) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Method method = obj.getClass().getDeclaredMethod("set" + StringUtil.toUpperCaseFirstOne(fieldName), value.getClass());
        method.invoke(obj, value);
        return true;
    }

    /**
     * 返回目标class，标注有指定注解的属性
     *
     * @param clazz      目标类型
     * @param annotation 目标注解
     * @return 非null，可能empty的Field集合
     */
    public static List<Field> getFieldByAnnotation(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation> annotation) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(annotation)) {
                list.add(field);
            }
        }
        return list;
    }


    /**
     * 如果目标类型属性标注了任意一个指定属性，返回此属性
     *
     * @param clazz       目标类型
     * @param annotations 注解集合
     * @return 非null，可能empty的Field集合
     */
    public static List<Field> getFieldByAnnotations(@Nonnull Class<?> clazz, @Nonnull Class<? extends Annotation>... annotations) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<Field> list = new ArrayList<>();
        for (Field field : declaredFields) {
            for (Class<? extends Annotation> temp : annotations) {
                if (field.isAnnotationPresent(temp)) {
                    list.add(field);
                }
            }
        }
        return list;
    }

    public static Object getInstance(Class<?> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor(null);
            return constructor.newInstance();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    private ReflectUtil() {
    }
}
