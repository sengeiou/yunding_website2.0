package com.yundingshuyuan.website.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.form.CoreForm;
import com.yundingshuyuan.website.pojo.Core;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
public interface ICoreService extends IService<Core> {

    /**
     * 打分
     * @param coreForm 分数表单
     * @return ServerResponse
     */
    ServerResponse score(CoreForm coreForm);
}
