package com.yundingshuyuan.website.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.enums.RightEnum;
import com.yundingshuyuan.website.enums.RoleEnum;
import com.yundingshuyuan.website.form.ContributionLogForm;
import com.yundingshuyuan.website.form.ListContributionLogForm;
import com.yundingshuyuan.website.pojo.ContributionLog;
import com.yundingshuyuan.website.pojo.Right;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IContributionLogService;
import com.yundingshuyuan.website.service.IUserRoleService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.util.RedisManager;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-10-20
 */
@RestController
@RequestMapping("/contribution-log")
public class ContributionLogController extends BaseController {
    private final
    IContributionLogService contributionLogService;
    private final
    IUserRoleService userRoleService;
    private final
    IUserService userService;
    private final
    RedisManager redisManager;

    public ContributionLogController(IContributionLogService contributionLogService, IUserRoleService userRoleService, IUserService userService, RedisManager redisManager) {
        this.contributionLogService = contributionLogService;
        this.userRoleService = userRoleService;
        this.userService = userService;
        this.redisManager = redisManager;
    }

    @ApiOperation("社群贡献榜打分")
    @PostMapping("/add")
    public ServerResponse add(@RequestBody ContributionLogForm contributionLogForm , HttpServletRequest request){
        final Calendar c = Calendar.getInstance();
        if (c.get(Calendar.DATE) == c.getActualMaximum(Calendar.DATE)) {//是最后一天
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.NOT_SCORE);
        }
            String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
            if (0 == userRoleService.getByUserId(userId).size()) {
                return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
            } else {
                return contributionLogService.add(contributionLogForm, userId);
            }

    }

    @ApiOperation("打分记录获取")
    @PostMapping("/list")
    public ServerResponse list(@RequestBody ListContributionLogForm contributionLogForm, HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        if(userService.authentication(userId,new Role(RoleEnum.ROLE_ENUM6.getId()),new Right(RightEnum.RIGHT_ENUM9.getId()))){
            IPage<ContributionLog> contributionLogPages = this.contributionLogService.page(new Page<>(contributionLogForm.getPageNum(),contributionLogForm.getPageSize()),new QueryWrapper<ContributionLog>().eq("time",contributionLogForm.getTime()));
            return ServerResponse.createBySuccess(contributionLogPages);
        }else {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
        }

    }

    @ApiOperation("获取我当月打分情况")
    @PostMapping("/getMyCanScore")
    public ServerResponse getMyCanScore(HttpServletRequest request){
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        List<ContributionLog> contributionLogs = redisManager.getValue(RedisPrefix.CONTRIBUTION_LOG,userId);
        if(contributionLogs== null){
            return ServerResponse.createBySuccess("当月未有打分记录");
        }else {
            Map<String,Object> map = new HashMap<>();
            map.put("contributionLogs",contributionLogs);
            int score = 0;
            for (ContributionLog contributionLog:contributionLogs){
                score = score+contributionLog.getScore();
            }
            map.put("scored",score);
            return ServerResponse.createBySuccess(map);
        }

    }
}
