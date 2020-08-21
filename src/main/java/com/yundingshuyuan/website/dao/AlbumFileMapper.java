package com.yundingshuyuan.website.dao;

import com.yundingshuyuan.website.pojo.AlbumFile;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 相册内容 Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface AlbumFileMapper extends BaseMapper<AlbumFile> {
    @Update("UPDATE album SET pic_num = pic_num+1 where id =#{albumId}")
    void albumNumAdd(String albumId);
    @Update("UPDATE album SET pic_num = pic_num-1 where id =#{albumId}")
    void albumNumMinus(String albumId);

}
