/**
 * FileName:UserInterceptor
 * Date:19-1-20 下午6:55
 * Description:用户模块拦截器
 */
package com.yundingshuyuan.website.interceptor;

import com.yundingshuyuan.website.constants.SysConstant;
import com.yundingshuyuan.website.repository.redis.IRedisRepository;
import com.yundingshuyuan.website.response.ServerResponse;
import com.yundingshuyuan.website.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class UserInterceptor implements HandlerInterceptor {
    @Autowired
    private IRedisRepository redisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        /*
         * 获取access_token
         */
        String accessToken = request.getHeader(SysConstant.HEADER_TOKEN);
        //未获取到token
        if (null == accessToken) {
            accessToken = request.getParameter(SysConstant.TOKEN_REQUEST_PARAM);
        }
        response.setHeader(SysConstant.HTTP_HEADER_CONTENT_TYPE, SysConstant.CONTENT_TYPE_APPLICATION_JSON);

        ServerResponse resultWrapper = null;
        if (null == accessToken) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resultWrapper = ServerResponse.createByErrorMessage("token认证失败");
            response.getWriter().println(JsonUtils.toJson(resultWrapper));
            return false;
        }
        String userId = redisRepository.findUserIdByAccessToken(accessToken);
        if (null == userId) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            resultWrapper = ServerResponse.createByErrorMessage("token无效");
            response.getWriter().println(JsonUtils.toJson(resultWrapper));
            return false;
        }
        request.getSession().setAttribute("UserID",userId);
        return true;
    }

}
