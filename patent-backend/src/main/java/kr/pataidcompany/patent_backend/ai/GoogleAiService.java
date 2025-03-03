package kr.pataidcompany.patent_backend.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleAiService implements AiService {

  @Value("${ai.google.key}")
  private String googleApiKey; // application.properties에서 주입

  private final RestTemplate restTemplate = new RestTemplate();

  // ★ 지침(txt 파일) 로드 유틸 클래스 (직접 작성 필요)
  @Autowired
  private AiGuidelinesLoader guidelinesLoader;

  /**
   * 발명 설명을 입력받아,
   * 구글 AI에 "특허 명세서 작성 지침" + 사용자 설명을 Prompt로 전달.
   */
  @Override
  public String generateIdeas(String inventionDescription) {
    // 1) txt 파일에서 지침 로드 (ex: "spec_drafting.txt")
    String guidelines = guidelinesLoader.loadGuidelines("spec_drafting.txt");

    // 2) 최종 Prompt 구성
    // 지침 + 사용자 입력을 합침
    String finalPrompt = guidelines + "\n\n[사용자 발명설명]\n" + inventionDescription;

    // 3) 실제 구글 AI API의 Endpoint URL (예: Vertex AI Text Generation)
    // 실제 문서는 Google Cloud 문서를 참고해 수정
    String url = "https://vertexai.googleapis.com/v1/projects/yourProjectId/locations/us-central1/publishers/google/models/text-bison:predict";

    // 4) 요청 바디(JSON). 구글 API 문서에 맞게 조정 필요
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
        """.formatted(finalPrompt);

    // 5) HTTP 헤더 설정
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(googleApiKey); // "Bearer " + googleApiKey
    headers.setContentType(MediaType.APPLICATION_JSON);

    // 6) RestTemplate로 POST 요청
    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

      if (response.getStatusCode().is2xxSuccessful()) {
        // 실제 응답 JSON을 파싱해야 함
        // 여기서는 전체 응답 JSON을 그대로 반환 (예시)
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
