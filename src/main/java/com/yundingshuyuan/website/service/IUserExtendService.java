package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.pojo.UserExtend;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-07
 */
public interface IUserExtendService extends IService<UserExtend> {
    /**
     * 对应的数+1
     * @param addByString 对应数据库相应的size字段
     * @param symbol 符号 + -
     * @param userId 对应的用户id
     */
    public void sizeUpdateByString(String addByString,String userId,String symbol);

}
