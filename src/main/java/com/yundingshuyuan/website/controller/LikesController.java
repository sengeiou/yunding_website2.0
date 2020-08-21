package com.yundingshuyuan.website.controller;


import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.form.LikesForm;
import com.yundingshuyuan.website.form.ListLikesForm;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.ILikesService;
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
 * 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/likes")
public class LikesController extends BaseController {
    private final
    ILikesService likesService;

    public LikesController(ILikesService likesService) {
        this.likesService = likesService;
    }

    @ApiOperation("点赞/取消点赞")
    @PostMapping("/addordelete")
    @Transactional
    public ServerResponse addordelete(@RequestBody LikesForm likesForm, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        likesForm.setUserId(userId);
        return likesService.add(likesForm);
    }

    @ApiOperation("查看当前用户是否点赞")
    @PostMapping
    public ServerResponse check(@RequestBody LikesForm likesForm,HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        likesForm.setUserId(userId);
        if(!likesService.check(likesForm)){
            return ServerResponse.createBySuccess("可以点赞");
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.ARTICLE_SUPPORTED);
        }
    }

    @ApiOperation("展示喜欢")
    @PostMapping("/list")
    public ServerResponse list(@RequestBody ListLikesForm likesForm){
        return likesService.list(likesForm);
    }


}
