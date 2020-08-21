package com.yundingshuyuan.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.ArticleCommentMapper;
import com.yundingshuyuan.website.dao.UserMapper;
import com.yundingshuyuan.website.form.ArticleCommentForm;
import com.yundingshuyuan.website.pojo.ArticleComment;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IArticleCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 评论表 服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@Service
public class ArticleCommentServiceImpl extends ServiceImpl<ArticleCommentMapper, ArticleComment> implements IArticleCommentService {
    @Autowired
    private ArticleCommentMapper articleCommentMapper;

    @Autowired
    private UserMapper userMapper;


    /**
     * 上传评论
     * @author Mr.h
     * @param articleComment
     * @return
     */
    @Override
    public ServerResponse upload(ArticleCommentForm articleComment, String userId, Integer floor) {

        try {
            ArticleComment articleComment1 = new ArticleComment();
            articleComment1.setArticleId(articleComment.getArticleId());
            articleComment1.setContent(articleComment.getContent());
            articleComment1.setUserId(userId);
            articleComment1.setFloor(floor + 1);
            articleComment1.setLabel(articleComment.getLabel());
            articleCommentMapper.insert(articleComment1);

            return ServerResponse.createBySuccess();
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 删除评论
     * @author Mr.h
     * @param id
     * @return
     */
    @Override
    public ServerResponse delete(String id) {
        try {
            ArticleComment articleComment = new ArticleComment();
            articleComment.setId(id);
            articleComment.setStatus(0);

            articleCommentMapper.updateById(articleComment);

            return ServerResponse.createBySuccessMessage("评论删除成功！");
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询评论总数
     * @author Mr.h
     * @return
     */
    @Override
    public Integer countAll(String articleId) {
        try {
            QueryWrapper<ArticleComment> ew = new QueryWrapper<>();
            ew.eq("article_id",articleId);
            Integer total = articleCommentMapper.selectCount(ew);

            return total;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 按时间顺序查询评论
     * @author Mr.h
     * @param articleId
     * @return
     */
    @Override
    public List<ArticleComment> selectAll(String articleId){
        try {
            List<ArticleComment> articleComment = articleCommentMapper.selectList(
                    new QueryWrapper<ArticleComment>()
                            .eq("article_id",articleId)
                            .eq("status",1)
                            .orderByDesc("create_time"));

            return articleComment;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 按时间顺序分页查询评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleComment> selectAllPage(String articleId, Integer pageNum, Integer pageSize) {
        try {
            Page<ArticleComment> page = new Page<>(pageNum,pageSize);
            IPage<ArticleComment> articleComments = articleCommentMapper.selectPage(page,
                    new QueryWrapper<ArticleComment>()
                            .eq("status",1)
                            .eq("article_id",articleId)
                            .orderByDesc("create_time"));
            System.out.println(articleComments.getRecords().toString()+"111111111");
            return articleComments;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 按楼层查询评论内容
     * @author Mr.h
     * @param articleId
     * @return
     */
    @Override
    public List<ArticleComment> selectALLDesc(String articleId) {
        try {
            List<ArticleComment> articleComment = articleCommentMapper.selectList(
                    new QueryWrapper<ArticleComment>()
                            .eq("status",1)
                            .eq("article_id",articleId)
                            .orderByAsc("create_time")
            );

            return articleComment;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 按楼层分页查询评论内容
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleComment> selectAllDescPage(String articleId, Integer pageNum, Integer pageSize) {
        try {
            Page<ArticleComment> page = new Page<>(pageNum,pageSize);
            IPage<ArticleComment> articleComments = articleCommentMapper.selectPage(page,
                    new QueryWrapper<ArticleComment>()
                            .eq("status",1)
                            .eq("article_id",articleId)
                            .orderByDesc("create_time"));

            return articleComments;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 按点赞数查询全部评论
     * @author Mr.h
     * @param articleId
     * @return
     */
    @Override
    public List<ArticleComment> selectAllBySupport(String articleId) {
        try {
            List<ArticleComment> articleComment = articleCommentMapper.selectList(
                    new QueryWrapper<ArticleComment>()
                            .eq("status",1)
                            .eq("article_id",articleId)
                            .orderByDesc("support_count")
            );

            return articleComment;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 按点赞数分页查询全部评论
     * @param articleId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleComment> selectAllBySupportPage(String articleId, Integer pageNum, Integer pageSize) {
        try {
            Page<ArticleComment> page = new Page<>(pageNum,pageSize);
            IPage<ArticleComment> articleComments = articleCommentMapper.selectPage(page,
                    new QueryWrapper<ArticleComment>()
                            .eq("status",1)
                            .eq("article_id",articleId)
                            .orderByDesc("support_count"));

            return articleComments;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

//    /**
//     * 通过评论用户userId和articleId增加点赞数
//     * @param articleComment
//     */
//    @Override
//    public ServerResponse addSupportCount(SupportCountForm articleComment) {
//
//        try {
//            ArticleComment support = articleCommentMapper.selectOne(
//                    new QueryWrapper<ArticleComment>()
//                            .eq("id",articleComment.getId())
//                            .select("support_count"));
//            ArticleComment articleComment1 = new ArticleComment();
//            articleComment1.setSupportCount(support.getSupportCount() + 1);
//
//            articleCommentMapper.update(articleComment1,
//                    new QueryWrapper<ArticleComment>()
//                            .eq("id",articleComment.getId()));
//
//            return ServerResponse.createBySuccessMessage("点赞成功！");
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
//        }
//
//    }

//    /**
//     * 通过评论用户userId和articleId删除点赞数
//     * @param articleComment
//     * @return
//     */
//    @Override
//    public ServerResponse minusSupport(SupportCountForm articleComment) {
//        try {
//            ArticleComment support = articleCommentMapper.selectOne(
//                    new QueryWrapper<ArticleComment>()
//                            .eq("id",articleComment.getId())
//                            .select("support_count"));
//
//            ArticleComment articleComment1 = new ArticleComment();
//            articleComment1.setSupportCount(support.getSupportCount() - 1);
//
//            articleCommentMapper.update(articleComment1,
//                    new QueryWrapper<ArticleComment>()
//                            .eq("id",articleComment.getId()));
//
//            return ServerResponse.createBySuccessMessage("删除点赞成功！");
//        } catch (Exception e){
//            e.printStackTrace();
//            throw new RuntimeException(e.getMessage());
//        }
//    }

    /**
     * 查询评论回复数
     * @param commentId
     * @return
     */
    @Override
    public Integer countReplyNum(String commentId) {
        ArticleComment articleComment = articleCommentMapper.selectById(commentId);
        if (articleComment != null){
            Integer replyNumber = articleComment.getReplyNum();
            return replyNumber;
        } else {
            return null;
        }

    }

    /**
     * 寻找最大评论数
     * @param articleId
     * @return
     */
    @Override
    public Integer getMaxFloor(String articleId) {

        List<ArticleComment> articleComment = articleCommentMapper.selectList(new QueryWrapper<ArticleComment>()
                .eq("article_id",articleId).orderByDesc("floor"));

        if (articleComment.isEmpty()){
            return 0;
        } else {
            ArticleComment articleComment1 = articleComment.get(0);
            Integer floor = articleComment1.getFloor();

            return floor;
        }


    }

    /**
     * 通过评论id获取评论人id
     * @param id
     * @return
     */
    @Override
    public String getCommentUserName(String id) {
        ArticleComment articleComment = articleCommentMapper.selectOne(new QueryWrapper<ArticleComment>()
                .eq("id",id));
        String userId = articleComment.getUserId();
        return userId;
    }

    /**
     * 增加评论回复数
     * @param commentId
     */
    @Override
    public void increaseReplyNum(String commentId, Integer replyNumber) {

        ArticleComment articleComment = new ArticleComment();
        articleComment.setId(commentId);
        articleComment.setReplyNum(replyNumber + 1);

        articleCommentMapper.updateById(articleComment);

    }

    /**
     * 减少评论回复数
     * @param commentId
     * @param replyNumber
     */
    @Override
    public void decreaseReplyNum(String commentId, Integer replyNumber) {

        ArticleComment articleComment = new ArticleComment();
        articleComment.setId(commentId);
        articleComment.setReplyNum(replyNumber - 1);

        articleCommentMapper.updateById(articleComment);

    }




}
