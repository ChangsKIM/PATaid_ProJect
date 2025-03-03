package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PatentDocumentRepository extends JpaRepository<PatentDocument, Long> {
    List<PatentDocument> findByUserId(Long userId);

    // 15일 자동 삭제용
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
