package com.yundingshuyuan.website.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author:leeyf
 * @create: 2019-03-26 16:03
 * @Description:
 */
@Data
public class UserInfoForm {
    @ApiModelProperty("真实姓名")
    private String name;
    @ApiModelProperty("方向")
    private String direction;
    @ApiModelProperty("昵称")
    private String nickname;
    @ApiModelProperty("性别")
    private String sex;
    @ApiModelProperty("学院")
    private String college;
    @ApiModelProperty("专业")
    private String professional;
    @ApiModelProperty("入学时间")
    private Integer admissionTime;
    @ApiModelProperty("签名")
    private String signature;
}