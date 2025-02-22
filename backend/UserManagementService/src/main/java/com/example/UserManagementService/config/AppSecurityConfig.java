package com.example.UserManagementService.config;

import com.example.UserManagementService.auth.UserSecurityContextFilter;
import com.example.UserManagementService.user.service.UserServiceInterface;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppSecurityConfig {

    private final UserServiceInterface userService;

    public AppSecurityConfig(UserServiceInterface userService) {
        this.userService = userService;
    }

    @Bean
    public FilterRegistrationBean<UserSecurityContextFilter> userContextFilter() {
        UserSecurityContextFilter filter = new UserSecurityContextFilter(userService); // Pass userService here
        FilterRegistrationBean<UserSecurityContextFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(filter);
        registrationBean.addUrlPatterns("/*");
        return registrationBean;
    }
}