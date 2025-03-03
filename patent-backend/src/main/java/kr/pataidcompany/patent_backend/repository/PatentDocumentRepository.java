package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatentDocumentRepository extends JpaRepository<PatentDocument, Long> {
    // 필요 시, 작성자(userId)로 검색하는 메서드도 추가 가능
    // List<PatentDocument> findByUserId(Long userId);
}

public interface PatentDocumentRepository extends JpaRepository<PatentDocument, Long> {
    List<PatentDocument> findByUserId(Long userId);

    // 15일 자동삭제를 위한 쿼리 메서드
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
