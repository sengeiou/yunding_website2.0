package com.yundingshuyuan.website.controller;


import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IRoleService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;
import com.yundingshuyuan.website.controller.support.BaseController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/role")
public class RoleController extends BaseController {
    private final
    IRoleService roleService;

    public RoleController(IRoleService roleService) {
        this.roleService = roleService;
    }


    @ApiOperation("获取全部角色")
    @GetMapping("/listRoles")
    public ServerResponse listRoles(){
        return ServerResponse.createBySuccess(roleService.list());
    }

}
