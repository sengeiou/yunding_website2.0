package com.yundingshuyuan.website.controller;


import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IRightService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.yundingshuyuan.website.controller.support.BaseController;

/**
 * <p>
 * 用户权限表 前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/right")
public class RightController extends BaseController {
    private final
    IRightService rightService;

    public RightController(IRightService rightService) {
        this.rightService = rightService;
    }
    @ApiOperation("获取全部权限")
    @GetMapping("/listRights")
    public ServerResponse listRights(){
        return ServerResponse.createBySuccess(rightService.list());


    }

    @ApiOperation("判断token是否有效")
    @GetMapping("/get")
    public ServerResponse get(){
        return ServerResponse.createBySuccess();
    }
}
