package kr.pataidcompany.patent_backend.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.nio.charset.StandardCharsets;

@Configuration
public class AiGuidelinesLoader {

    @Autowired
    private ResourceLoader resourceLoader;

    /**
     * 지정된 txt 파일을 읽어 문자열로 반환
     * 
     * @param filename 예: "spec_drafting.txt"
     * @return 파일 전체 텍스트
     */
    public String loadGuidelines(String filename) {
        try {
            // classpath:ai-guidelines/ 폴더 아래의 파일
            String path = "classpath:ai-guidelines/" + filename;
            Resource resource = resourceLoader.getResource(path);

            // InputStream으로 읽기
            try (var is = resource.getInputStream()) {
                byte[] bytes = is.readAllBytes();
                return new String(bytes, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // 실패 시, 에러 문구 반환 or 빈 문자열
            return "(지침 파일 로드 실패) " + e.getMessage();
        }
    }
}
