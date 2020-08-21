package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.enums.LikesTypeEnum;
import lombok.Data;

@Data
public class LikesForm {
    private String entityId;

    private String userId;

    private LikesTypeEnum type;
}
