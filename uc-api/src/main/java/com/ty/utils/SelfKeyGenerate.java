package com.ty.utils;

import com.alibaba.fastjson.JSON;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

//自定义key生成策略
@Component("selfKeyGenerate")
public class SelfKeyGenerate implements KeyGenerator {

    @Override
    public Object generate(Object target, Method method, Object... params) {
        return target.getClass().getSimpleName() + "#" + method.getName() + "(" + JSON.toJSONString(params) + ")";
    }
}
