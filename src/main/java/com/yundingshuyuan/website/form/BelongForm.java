package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.enums.LikesTypeEnum;
import lombok.Data;

@Data
public class BelongForm {
    private String entityId;

    private String userId;

    private LikesTypeEnum entityType;

    private String  description;
}
