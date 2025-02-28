package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 최상위 댓글
    List<Comment> findByBoardIdAndParentIdIsNullOrderByCreatedAtAsc(Long boardId);

    // 특정 parentId의 대댓글
    List<Comment> findByBoardIdAndParentIdOrderByCreatedAtAsc(Long boardId, Long parentId);
}
