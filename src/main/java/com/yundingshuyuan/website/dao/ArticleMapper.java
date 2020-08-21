package com.yundingshuyuan.website.dao;

import com.yundingshuyuan.website.pojo.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Update;

/**
 * <p>
 * 文章 Mapper 接口
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface ArticleMapper extends BaseMapper<Article> {
    /**
     * 文章views量+1，根据文章id
     * @param id 文章id
     */
    @Update("UPDATE article SET views = views + 1 where id =#{id}")
    void viewsAdd(String id);
}
