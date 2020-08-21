package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.enums.ArticleStatusEnum;
import lombok.Data;

@Data
public class AuthorizationArticleForm {
    String articleId;
    ArticleStatusEnum status;
}
