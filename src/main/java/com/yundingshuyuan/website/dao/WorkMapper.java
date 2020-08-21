package com.yundingshuyuan.website.dao;

import com.yundingshuyuan.website.pojo.Work;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface WorkMapper extends BaseMapper<Work> {

    @Update("UPDATE work SET views = views + 1 where id =#{id}")
    void viewsAddOne(String id);
}
