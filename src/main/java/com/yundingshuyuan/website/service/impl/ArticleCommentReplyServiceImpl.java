package com.yundingshuyuan.website.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.ArticleCommentReplyMapper;
import com.yundingshuyuan.website.form.ArticleCommentReplyForm;
import com.yundingshuyuan.website.pojo.ArticleCommentReply;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IArticleCommentReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文章评论回复表 服务实现类
 * </p>
 *
 * @author FantJ
 * @since 2019-01-27
 */
@Service
public class ArticleCommentReplyServiceImpl extends ServiceImpl<ArticleCommentReplyMapper, ArticleCommentReply> implements IArticleCommentReplyService {

    @Autowired
    private ArticleCommentReplyMapper articleCommentReplyMapper;

    /**
     * 上传评论回复
     * @param articleCommentReply
     * @return
     */
    @Override
    public ServerResponse upload(ArticleCommentReplyForm articleCommentReply, String userId) {

        try {
            ArticleCommentReply articleCommentReply1 = new ArticleCommentReply();
            articleCommentReply1.setCommentId(articleCommentReply.getCommentId());
            articleCommentReply1.setReplyCommentId(articleCommentReply.getReplyCommentId());
            articleCommentReply1.setRespUserId(articleCommentReply.getTargetUserId());
            articleCommentReply1.setUserId(userId);
            articleCommentReply1.setContent(articleCommentReply.getContent());

            articleCommentReplyMapper.insert(articleCommentReply1);
            return ServerResponse.createBySuccessMessage("评论回复成功！");

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 删除评论回复
     * @param id
     * @return
     */
    @Override
    public ServerResponse delete(Integer id) {
        try {
            articleCommentReplyMapper.deleteById(id);

            return ServerResponse.createBySuccessMessage("删除评论回复成功！");
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过评论id获取评论回复
     * @param commentId
     * @return
     */
    @Override
    public List<ArticleCommentReply> selectReply(Integer commentId) {
        try {
            List<ArticleCommentReply> articleCommentReplies = articleCommentReplyMapper.selectList(
                    new QueryWrapper<ArticleCommentReply>().eq("comment_id",commentId));

            return articleCommentReplies;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 通过评论id分页获取评论回复
     * @param commentId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public IPage<ArticleCommentReply> selectReplyPage(Integer commentId, Integer pageNum, Integer pageSize) {
        try {
            Page<ArticleCommentReply> page = new Page<>(pageNum,pageSize);
            IPage<ArticleCommentReply> articleCommentReplyIPage = articleCommentReplyMapper.selectPage(page,
                    new QueryWrapper<ArticleCommentReply>()
                            .eq("comment_id",commentId)
                            .eq("status",1)
                            .orderByDesc("create_time"));

            return articleCommentReplyIPage;
        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询该评论下是否有评论回复
     * @param id
     * @return
     */
    @Override
    public List<ArticleCommentReply> selectByCommentId(String id) {

        try {
            List<ArticleCommentReply> articleCommentReplyList = articleCommentReplyMapper.selectList(
                    new QueryWrapper<ArticleCommentReply>()
                    .eq("comment_id",id).eq("status",1).orderByAsc("create_time"));

            return articleCommentReplyList;

        } catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 更改评论回复状态
     * @param id
     */
    @Override
    public void updateStatus(String id) {

        try {
            ArticleCommentReply articleCommentReply = new ArticleCommentReply();
            articleCommentReply.setCommentId(id);
            articleCommentReply.setStatus(0);
            articleCommentReplyMapper.update(articleCommentReply,new QueryWrapper<ArticleCommentReply>()
                    .eq("comment_id",id));

        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * 通过回复评论id获取回复评论人id
     * @param id
     * @return
     */
    @Override
    public String getOneUserId(String id) {
        ArticleCommentReply articleCommentReply = articleCommentReplyMapper.selectOne(new QueryWrapper<ArticleCommentReply>()
                .eq("id",id));
        String userId = articleCommentReply.getUserId();
        return userId;
    }

}
