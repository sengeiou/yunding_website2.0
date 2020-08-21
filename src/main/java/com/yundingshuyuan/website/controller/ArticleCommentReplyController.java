package com.yundingshuyuan.website.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.form.ArticleCommentReplyForm;
import com.yundingshuyuan.website.form.CommentReplyPageForm;
import com.yundingshuyuan.website.form.DeleteCommentReplyForm;
import com.yundingshuyuan.website.pojo.ArticleCommentReply;
import com.yundingshuyuan.website.pojo.UserNotification;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IArticleCommentReplyService;
import com.yundingshuyuan.website.service.IArticleCommentService;
import com.yundingshuyuan.website.service.IUserNotificationService;
import com.yundingshuyuan.website.service.IUserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.yundingshuyuan.website.constants.CommentConstant.NOTIFICATION_CONTENT_REPLY;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/articleCommentReply")
public class ArticleCommentReplyController extends BaseController {
    @Autowired
    private IArticleCommentReplyService articleCommentReplyService;

    @Autowired
    private IArticleCommentService articleCommentService;

    @Autowired
    private IUserNotificationService userNotificationService;

    @Autowired
    private IUserService userService;


    /**
     * 上传评论回复
     * @param request
     * @param articleCommentReply
     * @return
     */
    @ApiOperation("上传评论回复")
    @PostMapping("/comment")
    public ServerResponse upload(HttpServletRequest request, @RequestBody ArticleCommentReplyForm articleCommentReply){
        //通过token获取回复评论人的id
        String userId = (String) request.getSession().getAttribute("UserID");

        //通过回复评论人的id获取回复评论人的用户名
        String name = userService.getUserNameById(userId);
        if (articleCommentReply == null){
            return ServerResponse.createByErrorMessage("上传评论回复为空！");

        } else {
            //向article_comment_reply表中添加回复评论的信息
            articleCommentReplyService.upload(articleCommentReply, userId);

            //通过评论id获取评论回复数
            Integer replyNumber = articleCommentService.countReplyNum(articleCommentReply.getCommentId());

            //增加评论回复数
            articleCommentService.increaseReplyNum(articleCommentReply.getCommentId(),replyNumber);

            //建立评论回复通知的对象
            UserNotification userNotification = new UserNotification();
            userNotification.setType(1);
            userNotification.setTitle(name+NOTIFICATION_CONTENT_REPLY);
            userNotification.setContent(articleCommentReply.getContent());
            userNotification.setUserId(articleCommentReply.getTargetUserId());
            userNotification.setTargetUserId(userId);

            //向user_notification表中添加消息提示
            userNotificationService.add(userNotification);
            return ServerResponse.createBySuccess("上传评论回复成功！");

        }

    }


    /**
     * 删除评论回复
     * @param deleteCommentReplyForm
     * @return
     */
    @ApiOperation("删除评论回复")
    @DeleteMapping("/comment")
    public ServerResponse delete(HttpServletRequest request, @RequestBody DeleteCommentReplyForm deleteCommentReplyForm){
        String id = deleteCommentReplyForm.getId();

        String userId = articleCommentReplyService.getOneUserId(id);
        String currentUserId = (String) request.getSession().getAttribute("UserID");

        if (userId.equals(currentUserId)){
            if (id == null){
                return ServerResponse.createByErrorMessage("删除评论回复失败！");
            } else {
                //从article_comment_reply表中获取comment_id
                ArticleCommentReply articleCommentReply = articleCommentReplyService.getById(deleteCommentReplyForm.getId());
                String commentId = articleCommentReply.getCommentId();

                //通过commentId获取评论回复数
                Integer replyNumber = articleCommentService.countReplyNum(commentId);

                //减少评论回复数
                articleCommentService.decreaseReplyNum(commentId,replyNumber);

                ArticleCommentReply articleCommentReply1 = new ArticleCommentReply();
                articleCommentReply1.setId(id);
                articleCommentReply1.setStatus(0);

                //从article_comment_reply表中更新状态
                articleCommentReplyService.updateById(articleCommentReply1);

                return ServerResponse.createBySuccessMessage("删除评论回复成功！");
            }
        } else {
            return ServerResponse.createByErrorMessage("删除评论本人的评论！");
        }


    }

    /**
     *通过评论id分页获取回复评论内容
     * @param commentReplyPageForm
     * @return
     */
    @ApiOperation("获取评论回复（分页）")
    @PostMapping("/selectByCommentIdPage")
    public ServerResponse selectReplyByCommentIdPage(@RequestBody CommentReplyPageForm commentReplyPageForm){

        Integer commentId = commentReplyPageForm.getCommentId();
        Integer pageNum = commentReplyPageForm.getPageNum();
        Integer pageSize = commentReplyPageForm.getPageSize();

        if (commentId != null || pageNum != null || pageSize != null){
            IPage<ArticleCommentReply> articleCommentReplyIPage = articleCommentReplyService.selectReplyPage(commentId,pageNum,pageSize);

            return ServerResponse.createBySuccess("成功获取评论回复！",articleCommentReplyIPage);
        } else {
            return ServerResponse.createByErrorMessage("获取评论回复失败！");
        }

    }
}
