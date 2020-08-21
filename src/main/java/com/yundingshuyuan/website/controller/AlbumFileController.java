package com.yundingshuyuan.website.controller;


import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.AlbumFileForm;
import com.yundingshuyuan.website.form.AlbumFilePageForm;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IAlbumFileService;
import com.yundingshuyuan.website.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 相册内容 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/album-file")
public class AlbumFileController extends BaseController {

    private final
    IAlbumFileService albumFileService;
    private final
    IUserService userService;

    public AlbumFileController(IAlbumFileService albumFileService, IUserService userService) {
        this.albumFileService = albumFileService;
        this.userService = userService;
    }

    @ApiOperation("为某相册添加/更换/隐藏/显示照片")
    @PostMapping("/save")
    @Transactional
    public ServerResponse addAlbumFile(@RequestBody AlbumFileForm albumFileForm, HttpServletRequest request) {
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM8.getId()));
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            //操作
            return albumFileService.save(albumFileForm);
        }
    }

    @ApiOperation("删除某图")
    @PostMapping("/delete")
    @Transactional
    public ServerResponse delete(@RequestBody AlbumFileForm albumFileForm,HttpServletRequest request){
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM8.getId()));
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            //操作
            return albumFileService.delete(albumFileForm);
        }
    }

    @ApiOperation("相册图片获取")
    @PostMapping("/list")
    public ServerResponse listAlbumFile(@RequestBody AlbumFilePageForm albumFilePageForm){
        return albumFileService.list(albumFilePageForm);
    }
}
