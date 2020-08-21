package com.yundingshuyuan.website.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:leeyf
 * @create: 2019-02-16 15:21
 * @Description:
 */
@Data
public class UserNotificationForm {

    @ApiModelProperty(value = "1:评论 2：私信 3:喜欢和赞 4:关注 5:其它提醒",required = true)
    private Integer type;

    @ApiModelProperty(value = "1:已读 2:未读")
    private Integer status;

    @ApiModelProperty("不传值")
    private String userId;

    private Integer pageNum;

    private Integer pageSize;
}