package com.yundingshuyuan.website.form;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author:leeyf
 * @create: 2019-04-08 20:19
 * @Description:
 */
@Data
public class UserForm {
    /**
     * 入学时间
     */
    private Integer admissionTime;

    /**
     * 性别
     */
    private String sex;

    /**
     * 部门：云顶院（5）、创客团（4）、极客团（3）、工程师（2）、学员（1）
     */
    @Size(min = 1,max = 5)
    private String department;

    /**
     * 方向：java、js、前端、python、机电、设计、秘书处
     */
    private String direction;
    /**
     * 期数
     */
    private Integer coreTime;

    Integer pageNum;
    Integer pageSize;
    String orderBy;
    Boolean order;

}