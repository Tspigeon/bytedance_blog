package com.ty.admin.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ty.admin.model.pojo.Permission;

import java.util.List;

public interface PermissionMapper extends BaseMapper<Permission> {

    List<Permission> findPermissionsByAdminId(Long adminId);


}

