package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.BelongForm;
import com.yundingshuyuan.website.pojo.Belong;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 * 归属表 服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface IBelongService extends IService<Belong> {
    /**
     * 添加归属
     * @param belongForm 归属表单
     * @return ServerResponse
     */
    ServerResponse add(BelongForm belongForm);

    /**
     * 条件获取用户归属
     * @param belongForm 归属表单
     * @return ServerResponse
     */
    ServerResponse listBelong(BelongForm belongForm);

    boolean add(Belong belong);
}
