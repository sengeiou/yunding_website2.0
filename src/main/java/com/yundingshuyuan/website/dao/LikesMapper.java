package com.yundingshuyuan.website.dao;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yundingshuyuan.website.pojo.Likes;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface LikesMapper extends BaseMapper<Likes> {

    @Select("select a.*,b.* from ${type} a,user b where a.id =#{entityId} and b.id=#{userId}")
    Boolean judge(String entityId, String type, String userId);

    @Update("UPDATE ${name} SET support = support+1 where id =#{entityId}")
    void add(String name,String entityId);

    @Update("UPDATE ${name} SET support = support-1 where id =#{entityId}")
    void minus(String name, String entityId);

    @Select("SELECT * FROM ${type} where id=#{entityId}")
    JSONObject listByType(String entityId, String type);

    @Select("SELECT * FROM ${type} where id=#{entityId} AND label=#{articleLabel}")
    JSONObject listArticleByType(String entityId, String type, String articleLabel);
}
