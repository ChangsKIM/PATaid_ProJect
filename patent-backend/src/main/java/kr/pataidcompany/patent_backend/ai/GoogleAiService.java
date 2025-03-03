package kr.pataidcompany.patent_backend.ai;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GoogleAiService implements AiService {

  @Value("${ai.google.key}")
  private String googleApiKey;

  private final RestTemplate restTemplate = new RestTemplate();

  @Autowired
  private AiGuidelinesLoader guidelinesLoader;

  @Override
  public String generateIdeas(String inventionDescription) {
    String guidelines = guidelinesLoader.loadGuidelines("spec_drafting.txt");
    String finalPrompt = guidelines + "\n\n[사용자 발명설명]\n" + inventionDescription;

    String url = "https://vertexai.googleapis.com/v1/projects/yourProjectId/locations/us-central1/publishers/google/models/text-bison:predict";

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

    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(googleApiKey);
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

    try {
      ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
      if (response.getStatusCode().is2xxSuccessful()) {
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
