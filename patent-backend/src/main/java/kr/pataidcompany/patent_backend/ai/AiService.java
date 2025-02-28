package kr.pataidcompany.patent_backend.ai;

/**
 * 공통 AI 서비스 인터페이스
 */
public interface AiService {

    /**
     * 발명(기술) 설명을 입력받아,
     * 구글 AI가 자동 생성한 아이디어(문구)를 반환
     */
    String generateIdeas(String inventionDescription);
}
