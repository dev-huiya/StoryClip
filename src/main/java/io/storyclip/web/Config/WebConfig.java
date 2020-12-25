package io.storyclip.web.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*")
                .maxAge(3000);
    }

    private static final String[] INTERCEPTOR_WHITE_LIST = {
        "/error",
        // /error 를 화이트리스트로 등록하지 않으면 서버 에러시 /error 호출하면서 인터셉터가 다시 실행된다
        // 2020-12-25 18:28 hw.kim
        "/", "/status",
        "/account/signup-check/email",
        "/account/signup",
        "/account/signin",
        "/auth/refresh"
    };

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtAuthInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(INTERCEPTOR_WHITE_LIST);
    }
}