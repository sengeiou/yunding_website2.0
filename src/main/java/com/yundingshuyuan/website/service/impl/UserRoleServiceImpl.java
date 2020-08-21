package com.yundingshuyuan.website.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.UserRoleMapper;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.pojo.UserRole;
import com.yundingshuyuan.website.service.IUserRoleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 用户角色表 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IUserRoleService {
    final
    UserRoleMapper userRoleMapper;

    public UserRoleServiceImpl(UserRoleMapper userRoleMapper) {
        this.userRoleMapper = userRoleMapper;
    }

    @Override
    public Role[] listRoleById(String userId) {

        return userRoleMapper.listRoleById(userId);
    }

    @Override
    public List<UserRole> getByUserId(String userId) {
        return this.userRoleMapper.getByUserId(userId);
    }
}
