package com.yundingshuyuan.website.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yundingshuyuan.website.constants.UserConstant;
import com.yundingshuyuan.website.controller.support.BaseController;
import com.yundingshuyuan.website.enums.ErrorCodeEnum;
import com.yundingshuyuan.website.enums.RedisPrefix;
import com.yundingshuyuan.website.form.*;
import com.yundingshuyuan.website.pojo.Core;
import com.yundingshuyuan.website.pojo.Role;
import com.yundingshuyuan.website.pojo.User;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.service.IUserRoleService;
import com.yundingshuyuan.website.service.IUserService;
import com.yundingshuyuan.website.util.OSSUtil;
import com.yundingshuyuan.website.util.RedisManager;
import com.yundingshuyuan.website.vo.UserInfoVO;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author leeyf
 * @since 2019-09-02
 */
@RestController
@RequestMapping("/user")
public class UserController extends BaseController {

    private final
    IUserService userService;
    private final
    IUserRoleService userRoleService;
    private final
    RedisManager redisManager;
    // one instance, reuse
    private final CloseableHttpClient httpClient = HttpClients.createDefault();


    public UserController(IUserService userService,
                          IUserRoleService userRoleService, RedisManager redisManager) {
        this.userService = userService;
        this.userRoleService = userRoleService;
        this.redisManager = redisManager;
    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    @Transactional
    public ServerResponse register(@RequestBody @Valid RegisterForm registerForm, BindingResult bindingResult) {
        validateParams(bindingResult);

        return userService.register(registerForm);
    }

    @ApiOperation("用户登录")
    @PostMapping("/login")
    @Transactional
    public ServerResponse login(@RequestBody @Valid LoginForm loginForm, BindingResult bindingResult) {
        validateParams(bindingResult);

        return userService.auth(loginForm);
    }

    @ApiOperation("手机登录")
    @PostMapping("/loginByPhone")
    @Transactional
    public ServerResponse loginByPhone(@RequestBody @Valid LoginForm loginForm, BindingResult bindingResult) {
        validateParams(bindingResult);

        return userService.authByPhone(loginForm);
    }

    @ApiOperation("头像上传")
    @PostMapping("/upload/image")
    public ServerResponse uploadImage(MultipartFile image, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute(UserConstant.USER_SESSION_NAME);
        User user = new User();
        try {
            String url = OSSUtil.uploadMultipartFile(image);
            user.setImage(url);
            user.setId(userId);
            userService.saveOrUpdate(user);
            return ServerResponse.createBySuccess("头像上传成功", url);
        } catch (IOException e) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IMAGE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("封面上传")
    @PostMapping("/upload/cover")
    public ServerResponse uploadCover(MultipartFile image, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("UserID");
        User user = new User();
        try {
            String url = OSSUtil.uploadMultipartFile(image);
            user.setCover(url);
            user.setId(userId);
            userService.saveOrUpdate(user);
            return ServerResponse.createBySuccess("封面上传成功", url);
        } catch (IOException e) {
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IMAGE_UPLOAD_ERROR);
        }
    }

    @ApiOperation("修改密码")
    @PostMapping("/update/password")
    @Transactional
    public ServerResponse updatePassword(@RequestBody PasswordForm passwordForm) {
        return userService.updatePassword(passwordForm);
    }

    @ApiOperation("保存用户信息")
    @PostMapping("/saveInfo")
    public ServerResponse saveInfo(@RequestBody UserInfoForm userInfoForm, HttpServletRequest httpServletRequest) {
        String userID = (String) httpServletRequest.getSession().getAttribute("UserID");
        User user = new User();
        user.setId(userID);
        BeanUtil.copyProperties(userInfoForm, user, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
        userService.saveOrUpdate(user);
        return ServerResponse.createBySuccess();
    }

    @ApiOperation("获取用户信息")
    @GetMapping("/getUserInfo/{userId}")
    public ServerResponse getUserInfo(@PathVariable String userId) {
        User user = userService.getById(userId);
        if(null==user){
            return ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.USER_NO_ERROR);
        }
        UserInfoVO userInfoVO = new UserInfoVO();
        BeanUtil.copyProperties(user, userInfoVO, CopyOptions.create().setIgnoreError(true).setIgnoreNullValue(true));
        if(null != user.getAdmissionTime()) {
            userInfoVO.setYunOld(LocalDateTime.now().getYear() - user.getAdmissionTime()+1);
        }
        Role[] roleList= userRoleService.listRoleById(userId);
        userInfoVO.setRoles(roleList);
        return ServerResponse.createBySuccess(userInfoVO);
    }

    @ApiOperation("分页条件获取学员榜")
    @PostMapping("/getStudentList")
    public ServerResponse getUserList(@RequestBody UserForm userForm) {
        //条件分页获取学员榜
        return userService.getStudentList(userForm);
    }

    @ApiOperation("分页条件获取用户列表")
    @PostMapping("/getUserList")
    public ServerResponse getUsers(@RequestBody UserForm userForm) {
        //条件分页获取学员榜
        return userService.getUserList(userForm);
    }

    @ApiOperation("根据用户id获取score")
    @PostMapping("/score")
    public ServerResponse getUserScore(@RequestBody ScoreForm scoreForm){
        List<Core> coreList = userService.getUserScore(scoreForm);
        return ServerResponse.createBySuccess(coreList);
    }

    @ApiOperation("根据用户id获取用户卡片")
    @PostMapping("/userCard")
    public ServerResponse getUserCard(@RequestBody UserCardForm userCardForm){
        return userService.getUserCard(userCardForm);
    }

    //浏览量+1
    @ApiOperation("网站浏览量+1")
    @GetMapping("/view")
    public ServerResponse viewsAddOne(HttpServletRequest request){
        LocalDateTime localDateTime = LocalDateTime.now();
        String ip= getClientIpAddr(request);//ip
        int port= request.getRemotePort();//port
        String userAgent = request.getHeader("User-Agent");//设备信息
        //查询ip对应地址
        String url = "http://ip-api.com/json/";//ip查询地址api
        url = url+ip+"?lang=zh-CN&fields=6025215";
        HttpGet httpGet =new HttpGet(url);
        String ipRealAddress = null;
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity =response.getEntity();
            ipRealAddress = EntityUtils.toString(httpEntity);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>();
        map.put("ip",ip);
        map.put("port",port);
        map.put("userAgent",userAgent);
        map.put("ip_real_address",ipRealAddress);
        map.put("time",localDateTime.toString());
        System.out.println(localDateTime+":当前访问用户IP为-"+ip);
        String newSessionId = request.getSession().getId();
        if(!redisManager.hasKey(RedisPrefix.SESSION_ID,newSessionId)) {
            redisManager.setValue(RedisPrefix.SESSION_ID, newSessionId, newSessionId);
            if(!redisManager.hasKey(RedisPrefix.IP,ip)){
                redisManager.setValue(RedisPrefix.IP, ip,map);
            }
            this.userService.viewsAddOne();
        }


        return ServerResponse.createBySuccess();
    }
     static String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

     static String getClientIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_CLUSTER_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_FORWARDED");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_VIA");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("REMOTE_ADDR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

        @ApiOperation("模糊搜索")
    @PostMapping("/search")
    public ServerResponse getUserByName(@RequestBody UserSearchForm userSearchForm ){
        return userService.getUserByName(userSearchForm);
    }


}
