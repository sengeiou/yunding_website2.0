package com.yundingshuyuan.website.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.yundingshuyuan.website.constants.ArticleConstant;
import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.pojo.*;
import com.yundingshuyuan.website.service.*;
import com.yundingshuyuan.website.util.RedisManager;
import com.yundingshuyuan.website.vo.ContributionLogVO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 定时任务
 */
//@RestController
@Component
public class ScheduledController {

    private final
    IUserService userService;
    private final
    ICoreService coreService;
    private final
    IArticleService articleService;
    private final
    IContributionLogService contributionLogService;
    private final
    IContributionService contributionService;
    private final
    RedisManager redisManager;
    private final
    IWorkService workService;

    public ScheduledController(IUserService userService, ICoreService coreService, IArticleService articleService, IContributionLogService contributionLogService, IContributionService contributionService, RedisManager redisManager, IWorkService workService) {
        this.userService = userService;
        this.coreService = coreService;
        this.articleService = articleService;
        this.contributionLogService = contributionLogService;
        this.contributionService = contributionService;
        this.redisManager = redisManager;
        this.workService = workService;
    }

    /**
     * 更新分数
     */
    @Scheduled(cron = "0 0 2 * * ? ")/*每天凌晨2点执行*/
    public void scheduled() {
        List<User> list = userService.list();
        for (User user : list) {
            Core coreServiceById = coreService.getById(user.getId());
            if (coreServiceById != null) {
                int calculationCore = calculation(coreServiceById);
                User userOne = new User(calculationCore);
                userService.update(userOne, new UpdateWrapper<User>().eq("id", user.getId()));
            }
        }
    }

    //计算用户分值
    private int calculation(Core coreServiceById) {
        return (coreServiceById.getCore1T()
                + coreServiceById.getCore2J()
                + coreServiceById.getCore3G()
                + coreServiceById.getCore4X()
                + coreServiceById.getCore5R()
                + coreServiceById.getCore6S()) / 6;
    }

    /**
     * 更新文章热度
     */
    @Scheduled(cron = "0 0 2 * * ? ")/*每天凌晨2点执行*/
    public void scheduled2() {
        List<Article> list = articleService.list();
        for (Article article : list) {
            Integer calculationTop = calculationTop(article);
            article.setHot(calculationTop);
            articleService.updateById(article);
        }
    }



    private Integer calculationTop(Article article) {
        return article.getViews() * ArticleConstant.ArticleView
                + article.getSupport() * ArticleConstant.ArticleSupport
                + article.getComments() * ArticleConstant.ArticleComment;
    }

    /**
     * 1.在数据库创建每天的浏览记录
     * 2.清楚前一天session缓存记录
     */
    //@Scheduled(cron = "0 0 0 * * ? ")/*每天凌晨0点执行*/
    @Scheduled(cron = "1 0 0 * * ? ")/*每天凌晨0点0分1s开始执行*/
    @Transactional
    public void scheduled3() {
        userService.addSiteInfo();
        redisManager.deleteBatch(RedisPrefix.SESSION_ID.getPrefix(),"*");
    }

    /**
     * 计算社群贡献榜
     */
    //@GetMapping("/a")
    @Scheduled(cron = "1 0 0 28-31 * ? ") //每个月的28-31田0分1s
    public void scheduled4(){
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {//是最后一天
        int time = LocalDateTime.now().getYear()*100+LocalDateTime.now().getMonthValue();
        List<ContributionLog> contributionLogList = contributionLogService.list(new QueryWrapper<ContributionLog>().eq("time",time));
        Map<String, Integer> contributionLogMap = new HashMap();
        for (ContributionLog contributionLog:contributionLogList){
            boolean con = contributionLogMap.containsKey(contributionLog.getUserId());
            if (!con){
                contributionLogMap.put(contributionLog.getUserId(),contributionLog.getScore());
            }else {
                int oldScore= contributionLogMap.get(contributionLog.getUserId());
                contributionLogMap.replace(contributionLog.getUserId(),oldScore+contributionLog.getScore());
            }
        }
        //创建list
        List<ContributionLogVO> contributionLogVOList = new ArrayList<>();
        contributionLogMap.forEach((userId,score) -> {
            ContributionLogVO contributionLogVO = new ContributionLogVO();
            contributionLogVO.setUserId(userId);
            contributionLogVO.setScore(score);
            contributionLogVOList.add(contributionLogVO);
        });
        contributionLogVOList.sort((ContributionLogVO con1, ContributionLogVO con2) -> con2.getScore() - con1.getScore());
        //最大分值
        int score_max = contributionLogVOList.get(0).getScore();
        System.out.println(score_max);
        List<Contribution> contributions = new ArrayList<>();
        int times = 0;
            for (ContributionLogVO contributionLogVO : contributionLogVOList) {
                Contribution contribution = new Contribution();
                contribution.setUserId(contributionLogVO.getUserId());
                contribution.setTime(String.valueOf(time));
                contribution.setScore(Float.valueOf(contributionLogVO.getScore()));
                contribution.setUserName(this.userService.getUserNameById(contributionLogVO.getUserId()));
                contributions.add(contribution);
                times++;
                if (times == 5) {
                    break;
                }
            }
        for (ContributionLogVO contributionLogVO:contributionLogVOList){
            userService.updateScore(contributionLogVO.getUserId(),contributionLogVO.getScore());
        }
        for (Contribution contribution:contributions){
            contribution.setScore(contribution.getScore()/score_max);
            contributionService.save(contribution);
            userService.addContribution(time,contribution.getUserId());
        }
    }

        //清空redis
        redisManager.deleteBatch(RedisPrefix.CONTRIBUTION_LOG.getPrefix(),"");
    }

    @Scheduled(cron = "0 0 1 * * ? ")/*每天凌晨1点执行*/
    public void scheduled5() {
        List<Work> workList = workService.list();
        for (Work work:workList){
           Integer hot= work.getSupport()*10+work.getViews();
           work.setHot(hot);
        }
        workService.saveOrUpdateBatch(workList);
    }
}
