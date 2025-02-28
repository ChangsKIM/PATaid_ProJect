package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.Comment;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import kr.pataidcompany.patent_backend.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/board/{boardId}/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private UserRepository userRepo;

    /**
     * 1) 최상위 댓글 목록
     * GET /api/board/{boardId}/comments/root
     */
    @GetMapping("/root")
    public ResponseEntity<?> listRootComments(@PathVariable Long boardId) {
        List<Comment> list = commentService.getRootComments(boardId);
        return ResponseEntity.ok(list);
    }

    /**
     * 2) 특정 댓글의 대댓글 목록
     * GET /api/board/{boardId}/comments/{parentId}/replies
     */
    @GetMapping("/{parentId}/replies")
    public ResponseEntity<?> listChildComments(
            @PathVariable Long boardId,
            @PathVariable Long parentId) {
        List<Comment> list = commentService.getChildComments(boardId, parentId);
        return ResponseEntity.ok(list);
    }

    /**
     * 3) 댓글 작성(최상위 or 대댓글)
     * POST /api/board/{boardId}/comments
     */
    @PostMapping
    public ResponseEntity<?> createComment(
            @PathVariable Long boardId,
            @RequestBody CreateCommentRequest req,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Login required.");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        Comment c = new Comment();
        c.setBoardId(boardId);
        c.setWriterId(currentUser.getUserId());
        c.setParentId(req.getParentId()); // null => 최상위, not null => 대댓글
        c.setContent(req.getContent());

        Comment saved = commentService.createComment(c);
        return ResponseEntity.ok(saved);
    }

    /**
     * 4) 댓글 수정
     * PUT /api/board/{boardId}/comments/{commentId}
     */
    @PutMapping("/{commentId}")
    public ResponseEntity<?> updateComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            @RequestBody UpdateCommentRequest request,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Login required.");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }
        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        Comment updated = commentService.updateComment(
                commentId,
                request.getNewContent(),
                currentUser.getUserId(),
                isAdmin);
        if (updated == null) {
            return ResponseEntity.badRequest().body("No permission or comment not found.");
        }
        return ResponseEntity.ok(updated);
    }

    /**
     * 5) 댓글 삭제
     * DELETE /api/board/{boardId}/comments/{commentId}
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long boardId,
            @PathVariable Long commentId,
            Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Login required.");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }
        boolean isAdmin = "ROLE_ADMIN".equals(currentUser.getRole());

        boolean result = commentService.deleteComment(commentId, currentUser.getUserId(), isAdmin);
        if (!result) {
            return ResponseEntity.badRequest().body("No permission or comment not found.");
        }
        return ResponseEntity.ok("Comment deleted.");
    }

    // ====== DTO classes ======
    public static class CreateCommentRequest {
        private Long parentId;
        private String content;

        public Long getParentId() {
            return parentId;
        }

        public void setParentId(Long parentId) {
            this.parentId = parentId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }

    public static class UpdateCommentRequest {
        private String newContent;

        public String getNewContent() {
            return newContent;
        }

        public void setNewContent(String newContent) {
            this.newContent = newContent;
        }
    }
}
