package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PriorArtSearchReportRepository extends JpaRepository<PriorArtSearchReport, Long> {
    List<PriorArtSearchReport> findByUserId(Long userId);

    // 15일 자동 삭제용
    void deleteByCreatedAtBefore(LocalDateTime cutoff);
}
