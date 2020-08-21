package com.yundingshuyuan.website.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * @author:leeyf
 * @create: 2019-03-25 20:18
 * @Description:
 */
@Data
public class RegisterForm {
    @NotEmpty
    private String phone;

    private String password;

    private String phoneCode;

}