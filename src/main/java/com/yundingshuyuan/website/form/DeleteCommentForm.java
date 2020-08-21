package com.yundingshuyuan.website.form;

import lombok.Data;

@Data
public class DeleteCommentForm {

    String id;

    //已评论人的userId
    String userId;

}
