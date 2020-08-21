package com.yundingshuyuan.website.dao;

import com.yundingshuyuan.website.pojo.Video;
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
public interface VideoMapper extends BaseMapper<Video> {

    @Update("UPDATE video SET pv_num = pv_num + 1 where id =#{id}")
    void viewsAddOne(String id);
}
