package com.yundingshuyuan.website.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.LikesTypeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.WorkForm;
import com.yundingshuyuan.website.form.WorkPageForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.Likes;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import com.yundingshuyuan.website.service.ILikesService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.service.IWorkService;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/work")
public class WorkController extends BaseController {
    private final
    IWorkService workService;
    private final
    IUserService userService;
    private final
    IBelongService belongService;
    private final
    ILikesService likesService;

    public WorkController(IWorkService workService, IUserService userService, IBelongService belongService, ILikesService likesService) {
        this.workService = workService;
        this.userService = userService;
        this.belongService = belongService;
        this.likesService = likesService;
    }

    @ApiOperation("添加产品")
    @PostMapping("/add")
    @Transactional
    public ServerResponse addWork(@RequestBody WorkForm workForm, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM6.getId()));

        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {

            return workService.addWork(workForm);
        }
    }

    @ApiOperation("删除作品")
    @PostMapping("/delete")
    @Transactional
    public ServerResponse deleteWork(@RequestBody WorkForm workForm, HttpServletRequest request) {
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM6.getId()));

        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            if (!workService.removeById(workForm.getId())) {
                return ServerResponse.createByErrorMessage("删除作品错误");
            }
            QueryWrapper<Belong> belongQueryWrapper = new QueryWrapper<>();
            belongQueryWrapper.eq("entity_id", workForm.getId())
                    .eq("entity_type", LikesTypeEnum.work.name());
            if (!belongService.remove(belongQueryWrapper)) {
                return ServerResponse.createByErrorMessage("删除归属成员错误");
            }
            //删除所有喜欢这个作品的记录
            likesService.remove(new QueryWrapper<Likes>().eq("entity_id",workForm.getId()).eq("type",LikesTypeEnum.work.name()));
            return ServerResponse.createBySuccess();
        }

    }

    @ApiOperation("展示作品")
    @PostMapping("/list")
    public ServerResponse listWork(@RequestBody WorkPageForm workPageForm) {
        return workService.listByWorkForm(workPageForm);
    }


}
