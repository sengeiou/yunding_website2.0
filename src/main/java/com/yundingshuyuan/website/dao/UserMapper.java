package com.yundingshuyuan.website.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yundingshuyuan.website.pojo.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface UserMapper extends BaseMapper<User> {
    @Update("UPDATE site_admin SET user_count = user_count+1")
    void userCountPlusOne();

    @Select("SELECT next_val FROM user_sequence")
    String selectSequence();

    @Update("UPDATE user_sequence SET next_val = next_val+1")
    void updateSequence();

    @Select("SELECT name FROM user WHERE name=#{name}")
    String getUserByName(String name);


    @Insert("INSERT INTO site_admin (user_count,time,sum_site_view) Values (#{count},#{time},#{sumSiteView})")
    void addSiteInfo(Integer count,Integer time,Long sumSiteView);

    @Select("SELECT user.name FROM user WHERE user.id =#{userId} ")
    String selectNameById(String userId);

    @Update("UPDATE site_admin SET site_view = site_view +1 WHERE time=#{time}")
    void viewsAddOne(Integer time);

    @Select("SELECT * FROM site_admin WHERE time = #{time}")
    JSONObject selectSiteAdmin(Integer time);


    @Update("UPDATE user_extend SET popularity = popularity +#{score} where USER_id = #{userId}")
    void updateScore(String userId, Integer score);

    @Update("UPDATE user SET contribution =#{newContribution} where id =#{userId}")
    void addContribution(String newContribution, String userId);

    @Select("SELECT popularity FROM user_extend where user_id =#{id}")
    Integer getUserPopularity(String id);


}
