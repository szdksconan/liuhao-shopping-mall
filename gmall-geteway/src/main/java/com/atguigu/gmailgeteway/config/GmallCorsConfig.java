package com.atguigu.gmailgeteway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@Configuration
public class GmallCorsConfig {

    @Bean
    public CorsWebFilter crosWebFilter(){
        //cors 跨域配置对象
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedOrigin("http://localhost:1024");
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedHeader("*");


        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        //配置源对象
        corsConfigurationSource.registerCorsConfiguration("/**",corsConfiguration);
        CorsWebFilter corsWebFilter = new CorsWebFilter(corsConfigurationSource);
        return corsWebFilter;
    }

}
