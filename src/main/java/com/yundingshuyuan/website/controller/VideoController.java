package com.yundingshuyuan.website.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.LikesTypeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.VideoForm;
import com.yundingshuyuan.website.form.VideoPageForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.yundingshuyuan.website.pojo.Likes;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IBelongService;
import com.yundingshuyuan.website.service.ILikesService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.service.IVideoService;
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
@RequestMapping("/video")
public class VideoController extends BaseController {
    private final
    IVideoService videoService;
    private final
    IUserService userService;
    private final
    IBelongService belongService;
    private final
    ILikesService likesService;

    public VideoController(IVideoService videoService, IUserService userService, IBelongService belongService, ILikesService likesService) {
        this.videoService = videoService;
        this.userService = userService;
        this.belongService = belongService;
        this.likesService = likesService;
    }

    @ApiOperation("保存/更新Video")
    @PostMapping("/add")
    @Transactional
    public ServerResponse addVideo(@RequestBody VideoForm videoForm, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM6.getId()));

        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            return videoService.addVideo(videoForm);
        }
    }

    @ApiOperation("删除视频")
    @PostMapping("/delete")
    @Transactional
    public ServerResponse deleteVideo(@RequestBody VideoForm videoForm, HttpServletRequest request) {
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM6.getId()));

        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {

            if (!videoService.removeById(videoForm.getId())) {
                return ServerResponse.createByErrorMessage("删除video错误");
            }
            QueryWrapper<Belong> belongQueryWrapper = new QueryWrapper<>();
            belongQueryWrapper.eq("entity_id", videoForm.getId())
                    .eq("entity_type", LikesTypeEnum.video.name());
            if (!belongService.remove(belongQueryWrapper)) {
                return ServerResponse.createByErrorMessage("删除归属成员错误");
            }
            //删除所有喜欢这个文章的记录
            likesService.remove(new QueryWrapper<Likes>().eq("entity_id",videoForm.getId()).eq("type",LikesTypeEnum.video.name()));
            return ServerResponse.createBySuccess();
        }
    }

    @ApiOperation("展示视频")
    @PostMapping("/list")
    @Transactional
    public ServerResponse listVideo(@RequestBody VideoPageForm videoPageForm) {
        return videoService.listVideo(videoPageForm);
    }


}
