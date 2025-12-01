package com.ty.handler;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ty.domain.http.Result;
import com.ty.domain.pojo.SysUser;
import com.ty.domain.vo.ErrorCode;
import com.ty.service.LoginService;
import com.ty.utils.UserThreadLocal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@Slf4j
public class LoginInterceptor implements HandlerInterceptor {

    @Resource
    private LoginService loginService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //在执行方法之前执行
        /**
         * 1.需要判断请求的接口路径是否为HandlerMethod(controller方法)
         * 2.打印日志
         * 3.判断token是否为空, 如果为空 未登录
         * 4.如果token为不为空, 登录验证 checkToken
         * 5.认证成功 放行
         */
        if(!(handler instanceof HandlerMethod)) {
            //handler 可能是 RequestResourceHandler
            //springboot 程序访问静态资源 默认去classpath下的static目录查询
            return true;
        }

        String token = request.getHeader("Authorization");

        log.info("=============request start=================");
        String requestURI = request.getRequestURI();
        log.info("request uri:{}",requestURI);
        log.info("request method:{}",request.getMethod());
        log.info("token:{}",token);
        log.info("=============request end===================");

        SysUser sysUser = loginService.checkToken(token);
        if (Objects.isNull(sysUser)) {
            Result result = Result.fail(ErrorCode.NO_LOGIN.getCode(), "未登录");
            response.setContentType("application/json;charset=utf-8");
            response.getWriter().print(JSON.toJSONString(result));
            return false;
        }

        UserThreadLocal.put(sysUser);
        System.out.println("拦截器运行");

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        //如果不删除ThreadLocal中用完的信息 会有内存泄露的风险

        //ThreadLocal的 key为弱引用 value为线程变量的副本与线程同生命周期
        //垃圾回收期会回收弱引用对象 key被回收 但value仍然存在于内存中 导致内存泄露
        UserThreadLocal.remove();
    }
}
