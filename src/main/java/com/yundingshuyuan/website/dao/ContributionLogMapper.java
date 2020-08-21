package com.yundingshuyuan.website.dao;

import com.yundingshuyuan.website.pojo.ContributionLog;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-10-20
 */
public interface ContributionLogMapper extends BaseMapper<ContributionLog> {

    @Select("SELECT name FROM user WHERE id = #{respuserId}")
    String getUserName(String respuserId);
}
