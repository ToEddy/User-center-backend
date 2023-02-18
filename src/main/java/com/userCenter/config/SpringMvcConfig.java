package com.userCenter.config;

import com.userCenter.controller.Interceptor.ProjectInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author eddy
 * @createTime 2023/2/11
 */
@Configuration
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {
    @Resource
    private ProjectInterceptor projectInterceptor;

    /**
     * 静态资源过滤
     *
     * @param registry
     */
    /*@Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler().addResourceLocations();
    }*/

    /**
     * 拦截器配置
     * 拦截器链的运行规则：
     * 前一个拦截器的前置出现问题，会终止后面的拦截器的启动，并且以及运行的拦截器不会运行post，而是直接跳到after
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectInterceptor).addPathPatterns("/user/**");
    }
}
