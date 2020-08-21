package com.yundingshuyuan.website.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.exception.UserException;
import com.yundingshuyuan.website.form.ArticleCommentForm;
import com.yundingshuyuan.website.form.ArticleIdFormPage;
import com.yundingshuyuan.website.form.DeleteCommentForm;
import com.yundingshuyuan.website.pojo.Article;
import com.yundingshuyuan.website.pojo.ArticleComment;
import com.yundingshuyuan.website.pojo.ArticleCommentReply;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.response.ResponseCode;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.*;
import com.yundingshuyuan.website.vo.PageVO;
import com.yundingshuyuan.website.vo.SimpleArticleCommentVO;
import com.yundingshuyuan.website.vo.SimpleReplyVO;
import com.yundingshuyuan.website.vo.SimpleUserVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 文章评论 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-01-27
 */
@RestController
@RequestMapping("/articleComment")
public class ArticleCommentController extends BaseController {

    @Autowired
    private IArticleCommentService articleCommentService;

    @Autowired
    private IArticleCommentReplyService articleCommentReplyService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IArticleService articleService;



    /**
     * 上传评论
     * @author Mr.h
     * @param request
     * @param articleComment
     * @return
     */
    @ApiOperation("上传评论")
    @PostMapping("/comment")
    public ServerResponse uploadComment(HttpServletRequest request, @RequestBody ArticleCommentForm articleComment){

//        //通过文章id获取到作者id
//        Article article = articleService.getById(articleComment.getArticleId());
//        String respUserId = article.getAuthor();
//        System.out.println(respUserId);

        //通过token获取到评论者的id
        String userId = (String) request.getSession().getAttribute("UserID");
        System.out.println(userId);

        //通过评论者id得到评论者的用户名
        String name = userService.getUserNameById(userId);
        if (userId == null){
            throw new UserException(ErrorCodeEnum.PARAM_ERROR);
        }

        if (articleComment == null){
            return ServerResponse.createByErrorCodeMessage(
                    ResponseCode.ERROR.getCode(),"上传评论为空！");

        }else {
            Integer floors;
            Integer floor = articleCommentService.getMaxFloor(articleComment.getArticleId());

            if (floor == null){
                floors = 0;
            } else {
                floors = floor;
            }
            //向article_comment表中添加评论者的信息
            articleCommentService.upload(articleComment, userId, floors);

            return ServerResponse.createBySuccess("上传评论成功！");
        }

    }


    /**
     * 删除评论
     * @author Mr.h
     * @return
     */
    @ApiOperation("删除评论")
    @DeleteMapping("/comment")
    public ServerResponse deleteComment(HttpServletRequest request, @RequestBody DeleteCommentForm deleteCommentForm){
        String id = deleteCommentForm.getId();

        String userId = articleCommentService.getCommentUserName(id);
        System.out.println(userId);

        String currentUserId = (String) request.getSession().getAttribute("UserID");
        System.out.println(currentUserId);

        if (userId.equals(currentUserId)){

            if (id == null){
                return ServerResponse.createByErrorMessage("删除评论失败！");
            } else {
                //先在article_comment表中删除相应评论
                articleCommentService.delete(id);
                //再article_comment_reply表中查询该评论下的评论回复
                List<ArticleCommentReply> articleCommentReply = articleCommentReplyService.selectByCommentId(id);
                //如果有评论回复，则删除
                if (articleCommentReply != null){
                    //更改其状态
                    articleCommentReplyService.updateStatus(id);

                    return ServerResponse.createBySuccessMessage("删除评论成功！");

                } else {
                    return ServerResponse.createBySuccessMessage("删除评论成功！");

                }

            }

        } else {
            return ServerResponse.createByErrorMessage("只能删除本人的评论！");
        }


    }


    /**
     * 获取评论总数
     * @author Mr.h
     * @return
     */
    @ApiOperation("获取评论总数")
    @GetMapping("/count")
    public ServerResponse countAll(String articleId){
        if (articleId != null){
            Integer total = articleCommentService.countAll(articleId);
            return ServerResponse.createBySuccess("获取评论总数成功！",total);
        } else {
            return ServerResponse.createByErrorMessage("获取评论总数失败！");
        }
    }



    /**
     * 按时间顺序分页查询评论内容
     * @param articleIdFormPage
     * @return
     */
    @ApiOperation("按时间，楼层，点赞数查询评论")
    @PostMapping("/list")
    public ServerResponse selectCommentByTimePage(@RequestBody ArticleIdFormPage articleIdFormPage){

        String userId = articleIdFormPage.getUserId();
        String articleId = articleIdFormPage.getArticleId();
        Integer pageNum = articleIdFormPage.getPageNum();
        Integer pageSize = articleIdFormPage.getPageSize();
        String type = articleIdFormPage.getType();

        if (articleId != null || pageNum != null || pageSize != null || type != null){
            if (type.equals("new")){
                IPage<ArticleComment> articleCommentIPageNew = articleCommentService.selectAllPage(articleId,pageNum,pageSize);
                PageVO<SimpleArticleCommentVO> simpleArticleCommentVOPageVO = selectComment(articleCommentIPageNew,userId);
                return ServerResponse.createBySuccess("按时间顺序分页查询全部评论成功！",simpleArticleCommentVOPageVO);
            } else if (type.equals("floor")){
                IPage<ArticleComment> articleCommentIPageFloor = articleCommentService.selectAllDescPage(articleId,pageNum,pageSize);
                PageVO<SimpleArticleCommentVO> articleCommentIPage = selectComment(articleCommentIPageFloor,userId);
                return ServerResponse.createBySuccess("按楼层数分页查询评论成功！",articleCommentIPage);
            } else if (type.equals("support")){
                IPage<ArticleComment> articleCommentIPageSupport = articleCommentService.selectAllBySupportPage(articleId,pageNum,pageSize);
                PageVO<SimpleArticleCommentVO> articleComments = selectComment(articleCommentIPageSupport,userId);
                return ServerResponse.createBySuccess("按点赞数分页查询评论成功！",articleComments);
            } else {
                return ServerResponse.createByErrorMessage("查询评论失败！");
            }

        } else {
            return ServerResponse.createByErrorMessage("分页查询评论给失败！");
        }


    }



//    /**
//     * 通过用户评论userId和articleId增加评论点赞数
//     * @param request
//     * @param articleComment
//     * @return
//     */
//    @ApiOperation("评论点赞")
//    @PostMapping("/support")
//    public ServerResponse addSupportCount(HttpServletRequest request, @RequestBody SupportCountForm articleComment){
//
//        //通过token获取到评论者的id
//        String userId = (String) request.getSession().getAttribute("UserID");
//        if (userId == null || articleComment == null){
//            return ServerResponse.createByErrorMessage("点赞失败！");
//        } else {
//            String commentId = articleComment.getId();
//            //创建CommentSupport实体
//            ArticleCommentSupport commentSupport = new ArticleCommentSupport();
//            commentSupport.setCommentId(commentId);
//            commentSupport.setUserId(userId);
//
//            //保存在comment——support表中
//            commentSupportService.save(commentSupport);
//
//            articleCommentService.addSupportCount(articleComment);
//
//            return ServerResponse.createBySuccessMessage("点赞成功！");
//        }
//
//    }


//    /**
//     * 通过用户评论userId和articleId减少评论点赞数
//     * @param request
//     * @param articleComment
//     * @return
//     */
//    @ApiOperation("删除点赞")
//    @DeleteMapping("/support")
//    public ServerResponse minusSupportCount(HttpServletRequest request, @RequestBody SupportCountForm articleComment){
//
//        //通过token获取到评论者的id
//        String userId = (String) request.getSession().getAttribute("UserID");
//
//        if (articleComment.getId() != null || userId != null){
//            //article_comment表中点赞数-1
//            articleCommentService.minusSupport(articleComment);
//            String commentId = articleComment.getId();
//
//            ArticleCommentSupport commentSupport = new ArticleCommentSupport();
//            commentSupport.setStatus(0);
//
//            try{
//                commentSupportService.update(commentSupport, new QueryWrapper<ArticleCommentSupport>()
//                        .eq("comment_id",commentId)
//                        .eq("user_id",userId));
//            } catch (Exception e){
//                e.printStackTrace();
//                throw new NullPointerException();
//            }
//
//
//            return ServerResponse.createBySuccessMessage("删除点赞成功！");
//        } else {
//            return ServerResponse.createByErrorMessage("删除点赞失败！");
//        }
//
//    }


    /**
     * 按一定格式返回前端评论信息
     * @param articleCommentIPage
     * @return
     */
    public PageVO<SimpleArticleCommentVO> selectComment(IPage<ArticleComment> articleCommentIPage, String currentUserId){

        //创建PageVO对象
        PageVO<SimpleArticleCommentVO> simpleArticleCommentVOPageVO = new PageVO<>();
        simpleArticleCommentVOPageVO.setTotal(articleCommentIPage.getTotal());
        simpleArticleCommentVOPageVO.setSize(articleCommentIPage.getSize());
        simpleArticleCommentVOPageVO.setPageCounts(articleCommentIPage.getPages());
        simpleArticleCommentVOPageVO.setCurrentPage(articleCommentIPage.getCurrent());

        List<SimpleArticleCommentVO> simpleArticleCommentVOS = toVOList(articleCommentIPage.getRecords(), SimpleArticleCommentVO.class);

        List<SimpleArticleCommentVO> list = new ArrayList<>();
        for (SimpleArticleCommentVO simpleArticle:simpleArticleCommentVOS) {

            System.out.println(simpleArticle.toString());
            String userId = simpleArticle.getUserId();

            //创建SimpleArticleComment对象
            SimpleArticleCommentVO<SimpleUserVO, SimpleReplyVO> simpleUserVOSimpleArticleCommentVO = new SimpleArticleCommentVO<>();
            //在user表中查找用户信息
            List<User> userInfo = userService.getUserInfo(userId);
            Article article = articleService.getById(simpleArticle.getArticleId());

            if (currentUserId == null){
                simpleUserVOSimpleArticleCommentVO.setHasSupport(false);
            } else {
//                Boolean hasSupport = commentSupportService.hasSupport(simpleArticle.getId(),currentUserId);
//                if (hasSupport == true){
//                    simpleUserVOSimpleArticleCommentVO.setHasSupport(true);
//                } else {
//                    simpleUserVOSimpleArticleCommentVO.setHasSupport(false);
//                }
            }

            simpleUserVOSimpleArticleCommentVO.setCreateTime(simpleArticle.getCreateTime());
            simpleUserVOSimpleArticleCommentVO.setSupportCount(simpleArticle.getSupportCount());

            simpleUserVOSimpleArticleCommentVO.setReplyNum(simpleArticle.getReplyNum());
            simpleUserVOSimpleArticleCommentVO.setUserId(simpleArticle.getUserId());
            simpleUserVOSimpleArticleCommentVO.setArticleId(simpleArticle.getArticleId());
            simpleUserVOSimpleArticleCommentVO.setContent(simpleArticle.getContent());
            simpleUserVOSimpleArticleCommentVO.setId(simpleArticle.getId());
            simpleUserVOSimpleArticleCommentVO.setStatus(simpleArticle.getStatus());
            simpleUserVOSimpleArticleCommentVO.setFloor(simpleArticle.getFloor());
            //将userInfo转换为SimpleUserVO
            List<SimpleUserVO> simpleUserVOS = toVOList(userInfo, SimpleUserVO.class);
            simpleUserVOSimpleArticleCommentVO.setUserInfo(simpleUserVOS.get(0));

            String commentID = simpleArticle.getId();
            //在评论回复表中查询是否有评论回复
            List<ArticleCommentReply> articleCommentReplies = articleCommentReplyService.selectByCommentId(commentID);

            List<SimpleReplyVO> simpleReplyVOS = toVOList(articleCommentReplies, SimpleReplyVO.class);

            List<SimpleReplyVO> replyVOList = new ArrayList<>();
            for (SimpleReplyVO simpleReply:simpleReplyVOS) {

                String replyUserId = simpleReply.getUserId();
                List<User> replyUserInfo = userService.getUserInfo(replyUserId);
                List<User> respUserInfo = userService.getUserInfo(simpleReply.getRespUserId());

                SimpleReplyVO<SimpleUserVO,SimpleUserVO> simpleUserVOSimpleReplyVO = new SimpleReplyVO<>();
                simpleUserVOSimpleReplyVO.setCommentId(simpleReply.getCommentId());
                simpleUserVOSimpleReplyVO.setReplyCommentId(simpleReply.getReplyCommentId());
                simpleUserVOSimpleReplyVO.setContent(simpleReply.getContent());
                simpleUserVOSimpleReplyVO.setId(simpleReply.getId());
                simpleUserVOSimpleReplyVO.setStatus(simpleReply.getStatus());
                simpleUserVOSimpleReplyVO.setRespUserId(simpleReply.getRespUserId());
                simpleUserVOSimpleReplyVO.setUserId(simpleReply.getUserId());
                simpleUserVOSimpleReplyVO.setCreateTime(simpleReply.getCreateTime());

                List<SimpleUserVO> simpleUserVOS1 = toVOList(replyUserInfo, SimpleUserVO.class);
                List<SimpleUserVO> simpleUserVOS2 = toVOList(respUserInfo, SimpleUserVO.class);
                System.out.println(simpleUserVOS1.toString());
                simpleUserVOSimpleReplyVO.setUserInfo(simpleUserVOS1.get(0));
                simpleUserVOSimpleReplyVO.setRespUserInfo(simpleUserVOS2.get(0));

                replyVOList.add(simpleUserVOSimpleReplyVO);



            }
            simpleUserVOSimpleArticleCommentVO.setTopCommentList(replyVOList);

            //将simpleUserVOSimpleArticleCommentVO转换为列表
            list.add(simpleUserVOSimpleArticleCommentVO);
            System.out.println(simpleUserVOSimpleArticleCommentVO);
            System.out.println(list.toString());
        }


        simpleArticleCommentVOPageVO.setCommentList(list);
        return simpleArticleCommentVOPageVO;
    }



}
