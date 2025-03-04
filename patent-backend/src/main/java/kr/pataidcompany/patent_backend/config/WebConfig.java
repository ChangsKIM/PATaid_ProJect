package kr.pataidcompany.patent_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * 전역(Global) CORS 설정
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // 모든 URL 패턴에 대해
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("*") // GET, POST, PUT, DELETE 등
                .allowedHeaders("*")
                .allowCredentials(true); // 쿠키/세션 인증 허용 시 true
    }
}
