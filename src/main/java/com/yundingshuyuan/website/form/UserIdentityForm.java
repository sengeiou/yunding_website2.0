package com.yundingshuyuan.website.form;

import com.yundingshuyuan.website.pojo.Role;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @author:leeyf
 * @create: 2019-03-28 19:49
 * @Description:
 */
@Data
public class UserIdentityForm {

    private String phone;
    /**
     * 部门：云顶院（5）、创客团（4）、极客团（3）、秘书处（2）、学员（1）
     */
    private String department;

    private com.yundingshuyuan.website.pojo.Role[] roles;

    /**
     * 等级：1、2、3
     */
    private String level;
}