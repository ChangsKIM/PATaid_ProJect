package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.ai.AiService;
import kr.pataidcompany.patent_backend.ai.GoogleAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * AI 연동 컨트롤러 (구글 AI 기준)
 */
@RestController
@RequestMapping("/api/patent")
public class AiController {

    // GoogleAiService를 직접 주입
    // 만약 팩토리 방식으로 교체하고 싶다면, AiService 인터페이스를 주입받고
    // 환경변수로 분기하는 방법도 가능
    @Autowired
    private GoogleAiService googleAiService;

    /**
     * 예: 발명 설명을 받아 구글 AI가 아이디어를 생성
     * POST /api/patent/ai-ideas
     */
    @PostMapping("/ai-ideas")
    public ResponseEntity<?> generateIdeas(@RequestBody IdeaRequest request) {
        // 1) inventionDescription를 AI에 전달
        String ideas = googleAiService.generateIdeas(request.getInventionDescription());

        // 2) 결과 반환
        return ResponseEntity.ok(ideas);
    }

    // 요청 DTO
    public static class IdeaRequest {
        private String inventionDescription;

        public String getInventionDescription() {
            return inventionDescription;
        }

        public void setInventionDescription(String inventionDescription) {
            this.inventionDescription = inventionDescription;
        }
    }
}
