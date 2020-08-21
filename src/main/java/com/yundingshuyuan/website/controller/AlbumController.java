package com.yundingshuyuan.website.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.AlbumForm;
import com.yundingshuyuan.website.form.AlbumPageForm;
import com.yundingshuyuan.website.pojo.Album;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IAlbumFileService;
import com.yundingshuyuan.website.service.IAlbumService;
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
 * 相册 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/album")
public class AlbumController extends BaseController {

    private final
    IUserService userService;
    private final
    IAlbumService albumService;
    private final
    IAlbumFileService albumFileService;

    public AlbumController(IUserService userService, IAlbumService albumService, IAlbumFileService albumFileService) {
        this.userService = userService;
        this.albumService = albumService;
        this.albumFileService = albumFileService;
    }

    @ApiOperation("创建/更新/隐藏相册")
    @PostMapping("/add")
    @Transactional
    public ServerResponse createAlbum(@RequestBody AlbumForm albumForm, HttpServletRequest request) {
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM8.getId()));
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            //创建
            Album album = new Album();
            BeanUtil.copyProperties(albumForm, album, CopyOptions.create().setIgnoreNullValue(true).setIgnoreCase(true));
            boolean saveOrUpdate = albumService.saveOrUpdate(album);
            if (saveOrUpdate) {
                return ServerResponse.createBySuccess(album);
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    @ApiOperation("删除相册")
    @PostMapping("/delete")
    public ServerResponse deleteAlbum(@RequestBody AlbumForm albumForm, HttpServletRequest request) {
        //鉴权
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        boolean aBoolean = userService.authentication(userId, new Role(RoleEnum.ROLE_ENUM6.getId()), new Right(RightEnum.RIGHT_ENUM8.getId()));
        if (!aBoolean) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        } else {
            //删除相册
            boolean removeById = albumService.removeById(albumForm.getId());
            if (removeById) {
                return ServerResponse.createBySuccess();
            } else {
                return ServerResponse.createByError();
            }
        }
    }

    @ApiOperation("展示相册")
    @PostMapping("/list")
    public ServerResponse listAlbum(@RequestBody AlbumPageForm albumPageForm) {
        return albumService.list(albumPageForm);
    }


}
