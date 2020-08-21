package com.yundingshuyuan.website.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.form.ArticleCommentForm;
import com.yundingshuyuan.website.pojo.ArticleComment;
import com.yundingshuyuan.website.response.ServerResponse;

import java.util.List;

/**
 * <p>
 * 评论表 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IArticleCommentService extends IService<ArticleComment> {

    /**
     *上传评论
     * @author Mr.h
     * @param articleComment
     * @return
     */
    ServerResponse upload(ArticleCommentForm articleComment, String userId, Integer floor);

    /**
     * 删除评论
     * @param id
     * @return
     */
    ServerResponse delete(String id);


    /**
     * 获取评论总数
     * @return
     */
    Integer countAll(String articleId);


    /**
     * 按时间查询全部评论
     * @return
     */
    List<ArticleComment> selectAll(String articleId);


    /**
     * 按时间分页查询全部评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<ArticleComment> selectAllPage(String articleId, Integer pageNum, Integer pageSize);


    /**
     * 按楼层获取评论
     * @param articleId
     * @return
     */
    List<ArticleComment> selectALLDesc(String articleId);


    /**
     * 按楼层数分页查询全部评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<ArticleComment> selectAllDescPage(String articleId, Integer pageNum, Integer pageSize);


    /**
     * 按点赞数查询评论
     * @param articleId
     * @return
     */
    List<ArticleComment> selectAllBySupport(String articleId);


    /**
     * 按点赞数分页查询全部评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    IPage<ArticleComment> selectAllBySupportPage(String articleId, Integer pageNum, Integer pageSize);


    /*
     * 通过评论用户userId和articleId增加评论数
     * @param articleComment
     */
//    ServerResponse addSupportCount(SupportCountForm articleComment);


    /*
     * 通过评论用户userId和articleId删除评论点赞数
     * @param articleComment
     * @return
     */
//    ServerResponse minusSupport(SupportCountForm articleComment);

    /**
     * 增加评论回复数
     * @param commentId
     */
    void increaseReplyNum(String commentId, Integer replyNumber);

    /**
     * 减少评论回复数
     * @param commentId
     * @param replyNumber
     */
    void decreaseReplyNum(String commentId, Integer replyNumber);

    /**
     * 查询回复数
     * @param commentId
     */
    Integer countReplyNum(String commentId);

    /**
     * 寻找最大楼层数
     * @param articleId
     * @return
     */
    Integer getMaxFloor(String articleId);

    /**
     * 通过评论id获取评论人id
     * @param id
     * @return
     */
    String getCommentUserName(String id);
}
