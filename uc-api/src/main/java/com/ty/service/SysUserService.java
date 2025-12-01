package com.ty.service;

import com.ty.domain.http.Result;
import com.ty.domain.pojo.SysUser;
import com.ty.domain.vo.UserVo;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Transactional
public interface SysUserService {

    /**
     * 根据用户id查询用户
     * @param id
     * @return
     */
    SysUser findUserById(Long id);

    /**
     * 根据账户密码查询用户
     * @param account
     * @param password
     * @return
     */
    SysUser findUser(String account, String password);

    /**
     * 根据token查询用户信息
     * @param token
     * @return
     */
    Result findUserByToken(String token);

    /**
     * 根据账户查找用户
     * @param account
     * @return
     */
    SysUser findUserByAccount(String account);

    /**
     * 保存用户
     * @param sysUser
     */
    void save(SysUser sysUser);

    /**
     * 根据id查询UserVo
     * @param id
     * @return
     */
    UserVo findUserVoById(Long id);

    /**
     * 修改用户属性
     * @param info
     * @return
     */
    boolean modifyUser(Map<String, Object> info);
}
