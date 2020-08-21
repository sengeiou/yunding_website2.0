package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.ArticleForm;
import com.yundingshuyuan.website.form.ListArticleForm;
import com.yundingshuyuan.website.form.ListArticleRandomForm;
import com.yundingshuyuan.website.form.RemoveArticleForm;
import com.yundingshuyuan.website.pojo.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 * 文章 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IArticleService extends IService<Article> {

    /**
     * 上传文章到数据库
     * @param articleForm 文章表单
     * @return ServerResponse
     */
    ServerResponse uploadArticle(ArticleForm articleForm);

    /**
     * 判断当前用户是否有权限发该类文章
     * @param userId 当前用户id
     * @param label 文章类别
     * @return 是true or 否 false
     */
    Boolean auth(String userId, String label);

    /**
     *
     * @param listArticleForm 文章表单
     * @return ServerResponse
     */
    ServerResponse listArticle(ListArticleForm listArticleForm);

    /**
     * 删除文章
     * @param removeArticleForm 删除条件表单
     * @return ServerResponse
     */
    ServerResponse removeArticle(RemoveArticleForm removeArticleForm);

    /**
     * 随机获取文章
     * @param articleForm
     * @return
     */
    ServerResponse listArticleRandom(ListArticleRandomForm articleForm);

    /**
     * 置顶文章
     * @param articleForm
     * @return
     */
    ServerResponse topArticle(ArticleForm articleForm);
}
