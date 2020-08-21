package com.yundingshuyuan.website.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.form.StatusForm;
import com.yundingshuyuan.website.form.UserNotificationForm;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.pojo.UserNotification;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IUserNotificationService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.vo.UserNotificationVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-17
 */
@RestController
@RequestMapping("/user-notification")
public class UserNotificationController extends BaseController {
    private final
    IUserNotificationService userNotificationService;

    private final
    IUserService userService;

    public UserNotificationController(IUserNotificationService userNotificationService, IUserService userService) {
        this.userNotificationService = userNotificationService;
        this.userService = userService;
    }

    @ApiOperation("获取通知根据token(不传userId)")
    @PostMapping("/notifications")
    public ServerResponse getAll(HttpServletRequest request,
                                 @RequestBody UserNotificationForm userNotificationForm){
        String userID = (String) request.getSession().getAttribute("UserID");
        userNotificationForm.setUserId(userID);

        Page page = new Page<>(userNotificationForm.getPageNum(),userNotificationForm.getPageSize());

        UserNotification userNotification =new UserNotification();
        if(userNotificationForm.getType()==0){
            userNotificationForm.setType(null);
        }
        BeanUtil.copyProperties(userNotificationForm,userNotification);
        System.out.println(userNotification.toString());
        QueryWrapper<UserNotification> userNotificationQueryWrapper = new QueryWrapper<>(userNotification);
        userNotificationQueryWrapper.orderByDesc("create_time");
        IPage<UserNotification> userNotificationIPage= userNotificationService.page(page,userNotificationQueryWrapper);

        List<UserNotificationVO> userNotificationVOList = new ArrayList<>();
        for (UserNotification userNotification1:
                userNotificationIPage.getRecords()) {
            UserNotificationVO userNotificationVO = new UserNotificationVO();
            User user =userService.getById(userNotification1.getTargetUserId());
            BeanUtil.copyProperties(user,userNotificationVO);
            BeanUtil.copyProperties(userNotification1,userNotificationVO);

            userNotificationVOList.add(userNotificationVO);
        }

        return ServerResponse.createBySuccess(userNotificationVOList);
    }

    @ApiOperation("单条消息已读")
    @PostMapping("/isRead")
    public ServerResponse updateStatus(@RequestBody StatusForm statusForm){
        UserNotification userNotification = new UserNotification();
        userNotification.setId(statusForm.getNotificationId());
        userNotification.setStatus(1);

        userNotificationService.updateById(userNotification);
        return ServerResponse.createBySuccess(userNotification);
    }

    @ApiOperation("当前用户消息已读")
    @PostMapping("/isReadAll")
    public ServerResponse updateListStatus(HttpServletRequest request){
        String userID = (String) request.getSession().getAttribute("UserID");
        UserNotification userNotification = new UserNotification();
        UserNotification userNotification2 = new UserNotification();
        userNotification2.setStatus(1);
        userNotification.setUserId(userID);
        QueryWrapper<UserNotification> userNotificationQueryWrapper = new QueryWrapper<>(userNotification);
        userNotificationService.update(userNotification2,userNotificationQueryWrapper);
        return ServerResponse.createBySuccess();
    }

    @ApiOperation("消息删除(假删)")
    @PostMapping("/delete")
    public ServerResponse deleteNotification(@RequestBody StatusForm statusForm, HttpServletRequest request){
        String userID = (String) request.getSession().getAttribute("UserID");
        UserNotification userNotification = new UserNotification();
        UserNotification userNotification2 = new UserNotification();
        userNotification2.setStatus(-1);
        userNotification.setId(statusForm.getNotificationId());
        userNotification.setUserId(userID);

        QueryWrapper<UserNotification> queryWrapper = new QueryWrapper<>(userNotification);
        userNotificationService.update(userNotification2,queryWrapper);
        return ServerResponse.createBySuccess();
    }

}
