package com.yundingshuyuan.website.service.impl;

import com.yundingshuyuan.website.pojo.UserExtend;
import com.yundingshuyuan.website.dao.UserExtendMapper;
import com.yundingshuyuan.website.service.IUserExtendService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-07
 */
@Service
public class UserExtendServiceImpl extends ServiceImpl<UserExtendMapper, UserExtend> implements IUserExtendService {
    @Override
    public void sizeUpdateByString(String addByString,String userId,String symbol) {
        this.baseMapper.sizeUpdateByString(addByString,userId,symbol);
    }
}
