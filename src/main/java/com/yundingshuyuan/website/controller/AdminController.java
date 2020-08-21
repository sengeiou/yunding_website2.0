package com.yundingshuyuan.website.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.*;
import com.yundingshuyuan.website.pojo.*;
import com.yundingshuyuan.website.repository.redis.IdentityRepository;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.ICoreService;
import com.yundingshuyuan.website.service.IUserRightService;
import com.yundingshuyuan.website.service.IUserRoleService;
import com.yundingshuyuan.website.service.IUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author: leeyf
 * @create: 2019-03-10 22:23
 * @Description: 后台接口
 */
@Api(tags = "后台接口")
@RequestMapping("/admin")
@RestController
public class AdminController {
    private final IdentityRepository identityRepository;
    private final IUserService userService;
    private final IUserRoleService userRoleService;
    private final IUserRightService userRightService;
    private final ICoreService coreService;

    public AdminController(IdentityRepository identityRepository, IUserService userService, IUserRoleService userRoleService, IUserRightService userRightService, ICoreService coreService) {
        this.identityRepository = identityRepository;
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.userRightService = userRightService;
        this.coreService = coreService;
    }

    @ApiOperation("身份授权")
    @PostMapping("/authorization")
    @Transactional
    public ServerResponse authorization(@RequestBody UserIdentityForm userIdentityForm,  HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("UserID");
        /*管理员用户*/
        /*验证角色*/
        Role[] roles = identityRepository.findIdentityByUserId(userId);
        Right right = new Right();

        right.setId(RightEnum.RIGHT_ENUM5.getId());
        if (0 == roles.length) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USER_ROLE_ERROR);
        } else {
            for (Role role : roles) {
                for (int j = 0; j < userIdentityForm.getRoles().length; j++) {
                    /*
                     * 鉴权
                     */
                    if (!userService.authentication(userId, role, right)) {
                        return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
                    }
                    Boolean aBoolean = userService.authentication(roles, userIdentityForm.getRoles());
                    if (!aBoolean) {
                        return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
                    }
                }

            }
        }

        /*根据帐号获取被授权人ID*/
        User userServiceOne = userService.getOne(new QueryWrapper<User>()
                .eq("phone", userIdentityForm.getPhone()));
        if (null == userServiceOne) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USER_NO_ERROR);
        }
        //被授权userID
        String targetUserId = userServiceOne.getId();
        /*授予帐号权限权限*/
        User user = new User();
        user.setDepartment(userIdentityForm.getDepartment());
        user.setLevel(userIdentityForm.getLevel());
        user.setId(userServiceOne.getId());
        userService.updateById(user);
        /*授予帐号角色*/
        UserRole userRole = new UserRole();
        userRole.setUserId(userServiceOne.getId());
        //删除之前帐号角色
        QueryWrapper<UserRole> userRoleQueryWrapper = new QueryWrapper<UserRole>().setEntity(userRole);
        userRoleService.remove(userRoleQueryWrapper);
        for (int i = 0; i < userIdentityForm.getRoles().length; i++) {
            userRole.setRoleId(userIdentityForm.getRoles()[i].getId());
            if (null == userRoleService.getOne(userRoleQueryWrapper)) {
                userRoleService.save(userRole);
            }
            //每次权限授权前清除该用户权限
            QueryWrapper<UserRight> userRightQueryWrapper = new QueryWrapper<>();
            userRightQueryWrapper.eq("user_id", targetUserId);
            userRightService.remove(userRightQueryWrapper);
            switch (RoleEnum.valueOf("ROLE_ENUM" + userIdentityForm.getRoles()[i].getId())) {
                case ROLE_ENUM6:
                    UserRight userRight8 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM8.getId());
                    UserRight userRight7 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM7.getId());
                    UserRight userRight6 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM6.getId());
                    userRightService.save(userRight8);
                    userRightService.save(userRight7);
                    userRightService.save(userRight6);
                case ROLE_ENUM5:
                    UserRight userRight9 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM9.getId());
                    userRightService.save(userRight9);
                case ROLE_ENUM4:

                case ROLE_ENUM3:

                case ROLE_ENUM2:
                    UserRight userRight5 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM5.getId());
                    userRightService.save(userRight5);
                case ROLE_ENUM1:
                    UserRight userRight1 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM1.getId());
                    UserRight userRight2 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM2.getId());
                    UserRight userRight3 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM3.getId());
                    UserRight userRight4 = new UserRight(targetUserId, RightEnum.RIGHT_ENUM4.getId());
                    userRightService.save(userRight1);
                    userRightService.save(userRight2);
                    userRightService.save(userRight3);
                    userRightService.save(userRight4);
                    break;
                //case ROLE_ENUM0:
                //    UserRight userRight = new UserRight(targetUserId, RightEnum.RIGHT_ENUM3.getId());
                //    userRightService.save(userRight);
                //    break;
                default:
            }

        }
        return ServerResponse.createBySuccess();
    }

    @ApiOperation("文章审核")
    @PostMapping
    @Transactional
    public ServerResponse authorizationArticle(@RequestBody AuthorizationArticleForm authorizationArticleForm,HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        Boolean authentication= userService.authentication(userId,new Role(RoleEnum.ROLE_ENUM6.getId()),new Right(RightEnum.RIGHT_ENUM9.getId()));
        if(authentication) {
            return userService.authorizationArticle(authorizationArticleForm);
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        }
    }

    @ApiOperation("后台登录接口")
    @PostMapping("/login")
    public ServerResponse adminLogin(@RequestBody LoginForm loginForm){
        ServerResponse auth= userService.auth(loginForm);
        if(auth.isSuccess()){
            Map map = (Map) auth.getData();
            String userId= (String) map.get("userId");
            boolean authentication = userService.authentication(userId,new Role(RoleEnum.ROLE_ENUM6.getId()),new Right(RightEnum.RIGHT_ENUM9.getId()));
            if(authentication){
                return auth;
            }else {
                return ServerResponse.createByError();
            }
        }else {
            return ServerResponse.createByError();
        }
    }

    @ApiOperation("打分")
    @PostMapping("/score")
    @Transactional
    public ServerResponse score(@RequestBody CoreForm coreForm,HttpServletRequest request){
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId,new Role(RoleEnum.ROLE_ENUM6.getId()),new Right(RightEnum.RIGHT_ENUM9.getId()));
        if(aBoolean) {
            return  coreService.score(coreForm);
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        }
    }

    //获取网站后台数据
    @ApiOperation("获取20aabb(aa：年 bb:月)的浏览量")
    @PostMapping("/getSiteAdmin")
    public ServerResponse getSiteAdmin(@RequestBody SiteAdminForm siteAdminForm){
     return userService.getSiteAdmin(siteAdminForm);
    }
}

