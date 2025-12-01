package com.ty.admin.utils;

import lombok.extern.log4j.Log4j2;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Pattern;

@Log4j2
public class PropertyUtils {

    public static <T> boolean isAnyNull(T obj) {
        if(isNull(obj)) {
            return true;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f : fields) {
            f.setAccessible(true);
            Object value;
            try {
                value = f.get(obj);
                if(value == null) {
                    return true;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public static <T> boolean isAllNull(T obj) {
        if(isNull(obj)) {
            return true;
        }
        Field[] fields = obj.getClass().getDeclaredFields();
        for(Field f : fields) {
            f.setAccessible(true);
            Object value;
            try {
                value = f.get(obj);
                if(value != null) {
                    return false;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static <T> boolean isNull(T obj) {
        return Objects.isNull(obj);
    }

    //正则表达式
    private static final Pattern ID_PATTERN = Pattern.compile("(?i)id");

    public static <T> T transToModel(Class<T> clazz, Map<String, Object> map){
        Object obj = null;
        try {
            Constructor<?> constructor = clazz.getConstructor();
            obj = constructor.newInstance();
            Field[] fields = clazz.getDeclaredFields();

            for(Field f : fields) {
                f.setAccessible(true);
                if(map.containsKey(f.getName())) {
                    Object o = map.get(f.getName());
                    if(ID_PATTERN.matcher(f.getName()).find()) {
                        f.set(obj, Long.valueOf((Integer) o));
                    } else {
                        f.set(obj, o);
                    }
                }
            }
        } catch (Exception e) {
            log.warn(e.getMessage());
        }
        return (T) obj;
    }
}