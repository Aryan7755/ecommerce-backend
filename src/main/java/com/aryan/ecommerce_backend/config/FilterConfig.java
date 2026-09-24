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

        // Prevent Spring Boot from auto-registering this filter into the
        // servlet container's generic filter chain. Spring Security already
        // manages its lifecycle explicitly via SecurityConfig's
        // .addFilterBefore(...), so without this line the filter runs twice
        // per request.
        registrationBean.setEnabled(false);

        return registrationBean;
    }
}