package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    // 카테고리별 페이징 목록
    Page<Board> findByCategoryOrderByCreatedAtDesc(String category, Pageable pageable);

    // 검색: 제목 or 본문에 키워드가 포함된 것
    Page<Board> findByTitleContainingOrContentContainingIgnoreCase(
            String titleKeyword,
            String contentKeyword,
            Pageable pageable);
}
