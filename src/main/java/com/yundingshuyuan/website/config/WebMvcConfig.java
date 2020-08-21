/**
 * FileName:WebMvcConfig
 * Author:monitor
 * Date:19-1-21 上午8:42
 * Description:
 */
package com.yundingshuyuan.website.config;

import com.yundingshuyuan.website.interceptor.UserInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    private final UserInterceptor userInterceptor;

    public WebMvcConfig(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

//    @Autowired
//    AdminFilter adminFilter;
    /**
     * 跳过拦截
     * @param registry 注册
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/error",
                        "/swagger-resources/**",
                        "/webjars/**",
                        "/static/**",
                        "/druid/**",
                        "/webapp/**",
                        "/**.html",
                        "/v2/**",
                        "/swagger-ui.html",
                        "/user/register",
                        "/user/login",
                        "/user/loginByPhone",
                        "/user/update/password",
                        "/code/**",
                        "/user/getUserInfo/**",
                        "/user/getStudentList",
                        "/user/score",
                        "/user/userCard",
                        "/article/list",
                        "/belong/list",
                        "/likes/list",
                        "/video/list",
                        "/album/list",
                        "/album-file/list",
                        "/contribution/list",
                        "/work/list",
                        "/admin/login",
                        "/article/search",
                        "/user/search",
                        "/scheduled4",
                        "/user/view"
                );
//        registry.
//                addInterceptor(adminFilter).
//                addPathPatterns("/admin/**",
//                                "/video/**",
//                                "/file/**",
//                                "/album/**",
//                                "/article/**").
//                excludePathPatterns(
//                        "/admin/login",
//                        "/login",
//                        "/admin/viewPlusOne",
//                        "/article/getArticle/{label}/{offset}/{limit}",
//                        "/article/get/{id}",
//                        "/album/get/{pageNum}/{pageSize}",
//                        "/album/{id}",
//                        "/file/get/{albumId}",
//                        "/video/get/{pageNum}/{pageSize}",
//                        "/video/get/{id}",
//                        "/admin/authorization"
//
//                );
    }
    /*解决跨域*/
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.setAllowCredentials(true);
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
