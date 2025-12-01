package com.ty.service;

import com.ty.domain.http.Result;
import com.ty.domain.pojo.SysUser;
import com.ty.domain.vo.param.LoginParam;

public interface LoginService {

    /**
     * 登录功能
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    /**
     * 校验token 返回用户信息
     * @param token
     * @return
     */
    SysUser checkToken(String token);

    /**
     * 登出功能
     * @param token
     * @return
     */
    Result logout(String token);

    /**
     * 注册功能
     * @param loginParam
     * @return
     */
    Result register(LoginParam loginParam);
}
