package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.WorkForm;
import com.yundingshuyuan.website.form.WorkPageForm;
import com.yundingshuyuan.website.pojo.Work;
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
public interface IWorkService extends IService<Work> {

    /**
     * 添加产品
     * @param workForm 产品表单
     * @return ServerResponse
     */
    ServerResponse addWork(WorkForm workForm);

    ServerResponse listByWorkForm(WorkPageForm workPageForm);
}
