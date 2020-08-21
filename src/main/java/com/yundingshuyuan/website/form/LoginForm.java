package com.yundingshuyuan.website.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author:leeyf
 * @create: 2019-03-24 21:24
 * @Description:
 */
@Data
public class LoginForm {
    @NotEmpty(message = "账号不能为空")
    String username;

    @NotEmpty
    @Size(min = 6,max = 16,message = "密码长度8-16位")
    String password;
}