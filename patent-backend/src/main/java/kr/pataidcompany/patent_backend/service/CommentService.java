package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.Comment;
import kr.pataidcompany.patent_backend.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;

    // 댓글 생성(최상위 or 대댓글)
    public Comment createComment(Comment comment) {
        return commentRepo.save(comment);
    }

    // 최상위 댓글 목록
    public List<Comment> getRootComments(Long boardId) {
        return commentRepo.findByBoardIdAndParentIdIsNullOrderByCreatedAtAsc(boardId);
    }

    // 특정 댓글의 대댓글 목록
    public List<Comment> getChildComments(Long boardId, Long parentId) {
        return commentRepo.findByBoardIdAndParentIdOrderByCreatedAtAsc(boardId, parentId);
    }

    // 댓글 삭제 (본인 or 관리자)
    public boolean deleteComment(Long commentId, Long writerId, boolean isAdmin) {
        Comment c = commentRepo.findById(commentId).orElse(null);
        if (c == null)
            return false;
        if (!isAdmin && !c.getWriterId().equals(writerId)) {
            return false;
        }
        commentRepo.deleteById(commentId);
        return true;
    }

    // 댓글 수정 (본인 or 관리자)
    public Comment updateComment(Long commentId, String newContent, Long writerId, boolean isAdmin) {
        Comment c = commentRepo.findById(commentId).orElse(null);
        if (c == null)
            return null;
        if (!isAdmin && !c.getWriterId().equals(writerId)) {
            return null;
        }
        c.setContent(newContent);
        return commentRepo.save(c);
    }
}
