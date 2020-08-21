package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.LikesForm;
import com.yundingshuyuan.website.form.ListLikesForm;
import com.yundingshuyuan.website.pojo.Likes;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface ILikesService extends IService<Likes> {

    /**
     * 点赞
     * @param likesForm 点赞表单
     * @return ServerResponse
     */
    ServerResponse add(LikesForm likesForm);

    /**
     * 查看点赞已否
     * @param likesForm 点赞表单
     * @return ServerResponse
     */
    Boolean check(LikesForm likesForm);

    /**
     * 展示喜欢
     * @param listLikesForm 展示条件表单
     * @return ServerResponse
     */
    ServerResponse list(ListLikesForm listLikesForm);
}
