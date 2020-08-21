package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.pojo.Role;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IRoleService extends IService<Role> {
    /**
     * 检查角色是否有误
     * @param roles
     * @return false有误,true无误
     */
    Boolean checkRole(Role[] roles);
}
