package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.pojo.UserNotification;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-17
 */
public interface IUserNotificationService extends IService<UserNotification> {
    /**
     * 添加消息通知
     * @param userNotification
     */
    void add(UserNotification userNotification);

}
