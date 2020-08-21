package com.yundingshuyuan.website.controller;


import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.BelongForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.repository.redis.IdentityRepository;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.yundingshuyuan.website.controller.support.BaseController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 归属表 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/belong")
public class BelongController extends BaseController {

    private final
    IBelongService belongService;

    private final
    IdentityRepository identityRepository;


    public BelongController(IBelongService belongService, IdentityRepository identityRepository) {
        this.belongService = belongService;
        this.identityRepository = identityRepository;
    }

    @ApiOperation("添加/取消用户归属")
    @PostMapping("/add")
    @Transactional
    public ServerResponse add(@RequestBody BelongForm belongForm, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        Role[] roles = identityRepository.findIdentityByUserId(userId);
        for (Role role : roles) {
            if (role.getId().equals(RoleEnum.ROLE_ENUM6.getId())) {
                return belongService.add(belongForm);
            }
        }
        return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
    }

    @ApiOperation("获取用户归属")
    @PostMapping("/list")
    @Transactional
    public ServerResponse listBelong(@RequestBody BelongForm belongForm){
        return belongService.listBelong(belongForm);
    }

}
