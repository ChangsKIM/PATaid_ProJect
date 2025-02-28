package kr.pataidcompany.patent_backend.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * 구글 AI API(예: Vertex AI, PaLM API 등)를 호출하는 예시
 */
@Service
public class GoogleAiService implements AiService {

    @Value("${ai.google.key}")
    private String googleApiKey; // application.properties에서 주입

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public String generateIdeas(String inventionDescription) {
        // 1) 실제 구글 AI API의 Endpoint URL (예시)
        // 실제 API 문서(예: Vertex AI GenerateText) 참고
        String url = "https://vertexai.googleapis.com/v1/projects/yourProjectId/locations/us-central1/publishers/google/models/text-bison:predict";

        // 2) 요청 바디(예시)
        // 실제 구글 AI API의 요청 JSON 형식에 맞춰 작성
        // 아래는 단순 예시일 뿐, 실제 JSON 구조와 다를 수 있음
        String requestBody = """
                {
                  "instances": [
                    { "prompt": "%s" }
                  ],
                  "parameters": {
                    "temperature": 0.7,
                    "maxOutputTokens": 512
                  }
                }
                """.formatted(inventionDescription);

        // 3) 헤더 설정
        org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.add("Authorization", "Bearer " + googleApiKey);
        headers.add("Content-Type", "application/json");

        // 4) RestTemplate로 POST 요청
        org.springframework.http.HttpEntity<String> entity = new org.springframework.http.HttpEntity<>(requestBody,
                headers);

        try {
            org.springframework.http.ResponseEntity<String> response = restTemplate.postForEntity(
                    url, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                // 실제 응답 JSON을 파싱해야 함
                // 여기서는 예시로 전체 응답 JSON을 그대로 반환
                return response.getBody();
            } else {
                return "Error from Google AI: " + response.getStatusCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Exception occurred: " + e.getMessage();
        }
    }
}
