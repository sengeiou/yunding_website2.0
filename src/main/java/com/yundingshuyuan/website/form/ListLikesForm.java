package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.enums.LikesTypeEnum;
import lombok.Data;

@Data
public class ListLikesForm {
    private String entityId;

    private String userId;

    private LikesTypeEnum type;

    private String articleLabel;

    Integer pageNum;
    Integer pageSize;
}
