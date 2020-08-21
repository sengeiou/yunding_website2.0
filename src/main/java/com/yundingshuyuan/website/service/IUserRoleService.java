package com.yundingshuyuan.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.pojo.UserRole;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IUserRoleService extends IService<UserRole> {
    /**
     * 根据用户id获取当前用户角色组
     * @param userId
     * @return
     */
    Role[] listRoleById(String userId);


    List<UserRole> getByUserId(String userId);
}
