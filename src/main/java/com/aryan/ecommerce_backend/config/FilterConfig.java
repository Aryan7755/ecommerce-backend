package com.aryan.ecommerce_backend.config;


import com.aryan.ecommerce_backend.security.jwt.JwtAuthenticationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtFilterRegistration(
            JwtAuthenticationFilter jwtAuthenticationFilter) {

        FilterRegistrationBean<JwtAuthenticationFilter> registrationBean =
                new FilterRegistrationBean<>(jwtAuthenticationFilter);


        registrationBean.setEnabled(false);

        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<LoggingFilter> loggingFilterRegistration(
            LoggingFilter loggingFilter) {

        FilterRegistrationBean<LoggingFilter> registrationBean =
                new FilterRegistrationBean<>(loggingFilter);

        registrationBean.setEnabled(false);

        return registrationBean;
    }
}