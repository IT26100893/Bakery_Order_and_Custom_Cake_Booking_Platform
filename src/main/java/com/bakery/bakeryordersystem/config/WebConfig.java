package com.bakery.bakeryordersystem.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override public void addInterceptors(InterceptorRegistry r) {
        r.addInterceptor(new AuthInterceptor()).addPathPatterns("/admin/**", "/customer/**");
    }
}
