package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.*;
import com.yundingshuyuan.website.pojo.Core;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.pojo.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IUserService extends IService<User> {

    /**
     * 用户注册
     * @param registerForm 注册表单
     * @return ServerResponse
     */
    ServerResponse register(RegisterForm registerForm);

    /**
     * 用户认证
     * @param loginForm 登录表单
     * @return ServerResponse
     */
    ServerResponse auth(LoginForm loginForm);

    /**
     * 更改密码
     * @param passwordForm 密码表单
     * @return ServerResponse
     */
    ServerResponse updatePassword(PasswordForm passwordForm);

    /*
     * 手机验证码登录
     * @param loginForm
     * @return
     */
    ServerResponse authByPhone(LoginForm loginForm);

    /**
     * 鉴权
     * 当前用户是否是该角色，拥有该权限
     * @param userId 用户id
     * @param role 角色
     * @param right 权限
     * @return true,否 false
     */
    Boolean authentication(String userId, Role role, Right right);

    /**
     * 判断前者角色组中是否都高于后者的角色
     * @param roles 前者角色组
     * @param roles1 后者角色组
     * @return tof
     */
    Boolean authentication(Role[] roles, Role[] roles1);
    /**
     * //条件分页获取学员榜
     * @param userForm 用户表单
     * @return ServerResponse
     */
    ServerResponse getStudentList(UserForm userForm);

    /**
     * 根据用户id获取分数
     * @return List<Core>
     */
    List<Core> getUserScore(ScoreForm scoreForm);

    /**
     * @param userCardForm 用户卡片表单
     * @return 用户卡片VO类
     */
    ServerResponse getUserCard(UserCardForm userCardForm);

    /**
     * 授权文章发布
     * @param authorizationArticleForm 文章表单
     * @return ServerResponse
     */
    ServerResponse authorizationArticle(AuthorizationArticleForm authorizationArticleForm);

    /**
     * 鉴权
     * @param userId 用户id
     * @param right 用户权限
     * @return ture or false
     */
    boolean authentication(String userId, Right right);

    void addSiteInfo();

    String getUserNameById(String userId);

    /**
     * 获取用户全部信息
     * @param userId
     */
    List getUserInfo(String userId);

    /**
     * 网站浏览量+1
     */
    void viewsAddOne();

    /**
     * 获取网站信息
     * @param siteAdminForm
     * @return
     */
    ServerResponse getSiteAdmin(SiteAdminForm siteAdminForm);

    /**
     * 获取用户列表
     * @param userForm 用户表单
     * @return ServerResponse
     */
    ServerResponse getUserList(UserForm userForm);

    /**
     * 模糊查询用户通过姓名
     * @param userSearchForm
     * @return
     */
    ServerResponse getUserByName(UserSearchForm userSearchForm);


    void updateScore(String userId, Integer score);

    void addContribution(int time,String userId);

    /**
     * 获得用户累积积分
     * @param id
     * @return
     */
    Integer getUserPopularity(String id);
}
