package com.yundingshuyuan.website.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yundingshuyuan.website.pojo.UserExtend;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-07
 */
public interface UserExtendMapper extends BaseMapper<UserExtend> {

    @Update("UPDATE user_extend SET ${addByString} = ${addByString} ${symbol} 1 WHERE user_id =#{userId}")
    void sizeUpdateByString(String addByString,String userId,String symbol);
}
