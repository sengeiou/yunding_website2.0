package com.yundingshuyuan.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.form.ArticleCommentReplyForm;
import com.yundingshuyuan.website.pojo.ArticleCommentReply;
import com.yundingshuyuan.website.response.ServerResponse;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IArticleCommentReplyService extends IService<ArticleCommentReply> {

    /**
     * 上传评论回复
     * @param articleCommentReply
     * @return
     */
    ServerResponse upload(ArticleCommentReplyForm articleCommentReply, String userId);

    /**
     * 删除评论评论回复
     * @param id
     * @return
     */
    ServerResponse delete(Integer id);

    /**
     * 通过评论id获取回复评论内容
     * @param commentId
     * @return
     */
    List<ArticleCommentReply> selectReply(Integer commentId);

    /**
     * 通过评论id分页获取回复评论给内容
     * @param commentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<ArticleCommentReply> selectReplyPage(Integer commentId, Integer pageNum, Integer pageSize);

    /**
     * 查询该评论下是否有评论回复
     * @param id
     * @return
     */
    List<ArticleCommentReply> selectByCommentId(String id);

    /**
     * 更改评论回复状态
     * @param id
     */
    void updateStatus(String id);

    /**
     * 通过回复评论id获取回复评论人id
     * @param id
     * @return
     */
    String getOneUserId(String id);

}
