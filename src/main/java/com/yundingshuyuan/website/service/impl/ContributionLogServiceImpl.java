package com.yundingshuyuan.website.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yundingshuyuan.website.dao.ContributionLogMapper;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.form.ContributionLogForm;
import com.yundingshuyuan.website.pojo.ContributionLog;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IContributionLogService;
import com.yundingshuyuan.website.util.RedisManager;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author leeyf
 * @since 2019-10-20
 */
@Service
public class ContributionLogServiceImpl extends ServiceImpl<ContributionLogMapper, ContributionLog> implements IContributionLogService {
    private final
    RedisManager redisManager;

    public ContributionLogServiceImpl(RedisManager redisManager) {
        this.redisManager = redisManager;
    }

    @Override
    public ServerResponse add(ContributionLogForm contributionLogForm,String respuserId) {
        contributionLogForm.setRespUserId(respuserId);
        if(contributionLogForm.getUserId().equals(respuserId)){
            return ServerResponse.createByErrorMessage("不能给自己打分");
        }
        //获取姓名
        String respUserName = this.baseMapper.getUserName(respuserId);
        if(respUserName==null || respUserName.equals("")){
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.RESPUSERNAME_EMPTY);
        }
        contributionLogForm.setRespUserName(respUserName);
        List<ContributionLog> contributionLogs = redisManager.getValue(RedisPrefix.CONTRIBUTION_LOG,respuserId);
        if (contributionLogs == null){
            contributionLogs = new ArrayList<>();
            System.out.println("空指针");
            ContributionLog contributionLog = new ContributionLog();
            BeanUtil.copyProperties(contributionLogForm, contributionLog);
            contributionLog.setUserName(this.baseMapper.getUserName(contributionLogForm.getUserId()));
            contributionLog.setTime(LocalDateTime.now().getYear()*100+LocalDateTime.now().getMonthValue());
            contributionLogs.add(contributionLog);
        }else {
            //标志：数组中是否存在该得分人
            boolean flag = false;
            int score = 0;
            int index = 0;
            for (ContributionLog contributionLog : contributionLogs) {
                score = score+contributionLog.getScore();
                if (contributionLog.getUserId().equals(contributionLogForm.getUserId()) && contributionLog.getRespUserId().equals(respuserId)) {
                    flag = true;
                    break;
                }

                index++;
            }
            //总积分30
            if (score + contributionLogForm.getScore() > 30){
                return ServerResponse.createByErrorMessage("已打分数大于30");
            }
            //数组中有,则将分数累加
            if (flag) {
                ContributionLog contributionLog = contributionLogs.get(index);
                contributionLog.setScore(contributionLog.getScore() + contributionLogForm.getScore());
                contributionLogs.set(index, contributionLog);
            } else if (contributionLogs.size() < 3) {
                ContributionLog contributionLog = new ContributionLog();
                BeanUtil.copyProperties(contributionLogForm, contributionLog);
                contributionLog.setUserName(this.baseMapper.getUserName(contributionLogForm.getUserId()));
                contributionLog.setTime(LocalDateTime.now().getYear()*100+LocalDateTime.now().getMonthValue());
                contributionLogs.add(contributionLog);
            } else {
                return ServerResponse.createByErrorMessage("禁止打分");
            }
        }
        System.out.println(contributionLogs.isEmpty());

            boolean saveOrUpdateBatch= this.saveOrUpdateBatch(contributionLogs);
            if(saveOrUpdateBatch){
                redisManager.setValue(RedisPrefix.CONTRIBUTION_LOG,respuserId,contributionLogs);
                return ServerResponse.createBySuccessMessage("打分成功");
            }

        return ServerResponse.createByErrorMessage("打分失败");
    }
}
