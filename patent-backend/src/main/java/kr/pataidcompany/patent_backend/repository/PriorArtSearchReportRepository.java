package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PriorArtSearchReportRepository extends JpaRepository<PriorArtSearchReport, Long> {
    // 필요 시 userId로 검색
    // List<PriorArtSearchReport> findByUserId(Long userId);
}
