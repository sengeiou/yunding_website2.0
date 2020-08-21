package com.yundingshuyuan.website.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.UserNotificationMapper;
import com.yundingshuyuan.website.pojo.UserNotification;
import com.yundingshuyuan.website.service.IUserNotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-17
 */
@Service
public class UserNotificationServiceImpl extends ServiceImpl<UserNotificationMapper, UserNotification> implements IUserNotificationService {
    @Autowired
    UserNotificationMapper userNotificationMapper;

    @Override
    public  void add(UserNotification userNotification) {
        userNotificationMapper.insert(userNotification);
    }

}
