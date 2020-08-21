package com.yundingshuyuan.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.dao.RoleMapper;
import com.yundingshuyuan.website.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.scripting.xmltags.ForEachSqlNode;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

    @Override
    public Boolean checkRole(Role[] roles) {

        for (int i = 0; i < roles.length; i++) {
            Role role= this.getById(roles[i].getId());
            if(null == role){
                return false;
            }
        }
        return true;
    }
}
