//package com.yundingshuyuan.website.interceptor;
//
//
//import com.yundingshuyuan.website.constants.SysConstant;
//import com.yundingshuyuan.website.constants.UserConstant;
//import com.yundingshuyuan.website.enums.ErrorCodeEnum;
//import com.yundingshuyuan.website.pojo.User;
//import com.yundingshuyuan.website.repository.redis.IRedisRepository;
//import com.yundingshuyuan.website.response.ServerResponse;
//import com.yundingshuyuan.website.service.IUserService;
//import com.yundingshuyuan.website.util.JsonUtils;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Optional;
//
///**
// * @author:leeyf
// * @create: 2019-03-12 12:29
// * @Description:
// */
//@Slf4j
//@Component
//public class AdminFilter implements HandlerInterceptor {
//
//    @Autowired
//    private IRedisRepository redisRepository;
//    @Autowired
//    private IUserService userService;
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        /**
//         * 获取access_token
//         */
//        String accessToken = request.getHeader(SysConstant.HEADER_TOKEN);
//        //未获取到token
//        if (null == accessToken) {
//            accessToken = request.getParameter(SysConstant.TOKEN_REQUEST_PARAM);
//        }
//        response.setHeader(SysConstant.HTTP_HEADER_CONTENT_TYPE, SysConstant.CONTENT_TYPE_APPLICATION_JSON);
//
//        ServerResponse resultWrapper = null;
//        if (null == accessToken) {
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            resultWrapper = ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.ERROR_TOKEN);
//            response.getWriter().println(JsonUtils.toJson(resultWrapper));
//            return false;
//        }
//        String userId = redisRepository.findUserIdByAccessToken(accessToken);
//        if (null == userId) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            resultWrapper = ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.ERROR_TOKEN);
//            response.getWriter().println(JsonUtils.toJson(resultWrapper));
//            return false;
//        }
//        System.out.println("---------------adminFifter------------------");
//        request.getSession().setAttribute("UserID",userId);
//        System.out.println(userId);
//        User userServiceById= userService.getById(userId);
//        //Optional<UserIdentity> userIdentityOptional = userIdentityRepository.findById(userId);
//        System.out.println("用户权限"+Integer.valueOf(userServiceById.getDepartment()));
//        //System.out.println("用户等级"+Integer.valueOf(userServiceById.getLevel()));
//        if(userServiceById==null){
//            response.setStatus(HttpServletResponse.SC_CREATED);
//            resultWrapper = ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_ERROR);
//            response.getWriter().println(JsonUtils.toJson(resultWrapper));
//            return false;
//        }else {
//            if(Integer.valueOf(userServiceById.getDepartment())< UserConstant.IDENTITY_DETAIL){
//                response.setStatus(HttpServletResponse.SC_ACCEPTED);
//                resultWrapper = ServerResponse.createByErrorCodeMessage(ErrorCodeEnum.IDENTITY_UN_ADMIN);
//                response.getWriter().println(JsonUtils.toJson(resultWrapper));
//                return false;
//            }else {
//                return true;
//            }
//        }
//
//    }
//
//}