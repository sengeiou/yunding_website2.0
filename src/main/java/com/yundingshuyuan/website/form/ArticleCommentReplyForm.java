package com.yundingshuyuan.website.form;

import lombok.Data;


@Data
public class ArticleCommentReplyForm {

    String commentId;
    String replyCommentId;
    String targetUserId;
    String content;

}
