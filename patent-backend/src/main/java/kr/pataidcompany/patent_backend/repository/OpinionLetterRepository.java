package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.OpinionLetter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OpinionLetterRepository extends JpaRepository<OpinionLetter, Long> {
    // 필요 시 userId로 검색
    // List<OpinionLetter> findByUserId(Long userId);
}
