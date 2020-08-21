package com.yundingshuyuan.website.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.form.ContributionForm;
import com.yundingshuyuan.website.pojo.Contribution;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IContributionService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.vo.ContributionVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-10-20
 */
@RestController
@RequestMapping("/contribution")
public class ContributionController extends BaseController {

    private final
    IContributionService contributionService;

    public ContributionController(IContributionService contributionService, IUserService userService) {
        this.contributionService = contributionService;
        this.userService = userService;
    }
    private final
    IUserService userService;

    @ApiOperation("获取贡献榜")
    @PostMapping("/list")
    public ServerResponse getContribution(@RequestBody ContributionForm contributionForm ){
        List<Contribution> contributions= this.contributionService.list(new QueryWrapper<Contribution>().eq("time",contributionForm.getTime()));
        List<ContributionVO> contributionVOList = new ArrayList<>();

        contributions
                .forEach(contribution -> {
                    ContributionVO contributionVO = new ContributionVO();
                    User user= userService.getById(contribution.getUserId());
                    //累积积分
                    Integer allScore = userService.getUserPopularity(user.getId());
                    contributionVO.setAllScore(allScore);
                    BeanUtil.copyProperties(user,contributionVO);
                    BeanUtil.copyProperties(contribution,contributionVO);
                    contributionVOList.add(contributionVO);
        });


        return ServerResponse.createBySuccess(contributionVOList);
    }

}
