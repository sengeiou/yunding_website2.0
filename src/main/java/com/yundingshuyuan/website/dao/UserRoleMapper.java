package com.yundingshuyuan.website.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.pojo.UserRole;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>
 * 用户角色表 Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {

    @Select("select r.* from role r ,user_role s  where s.user_id=#{userId} and r.id = s.role_id")
    Role[] listRoleById(String userId);

    @Select("SELECT * FROM user_role WHERE user_id = #{userId}")
    List<UserRole> getByUserId(String userId);
}
