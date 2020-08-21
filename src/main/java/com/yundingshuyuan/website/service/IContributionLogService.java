package com.yundingshuyuan.website.service;

import com.yundingshuyuan.website.form.ContributionLogForm;
import com.yundingshuyuan.website.pojo.ContributionLog;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yundingshuyuan.website.response.ServerResponse;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author leeyf
 * @since 2019-10-20
 */
public interface IContributionLogService extends IService<ContributionLog> {

    /**
     * 给社群贡献打分
     * @param contributionLogForm 贡献表单
     * @param respuserId 使用接口的用户id
     * @return ServerResponse
     */
    ServerResponse add(ContributionLogForm contributionLogForm,String respuserId);
}
