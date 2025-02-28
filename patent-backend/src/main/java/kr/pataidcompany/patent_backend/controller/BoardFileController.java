package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.Board;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.BoardRepository;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

/**
 * 게시판 글 파일첨부를 별도 컨트롤러로 관리
 */
@RestController
@RequestMapping("/api/board")
public class BoardFileController {

    @Value("${upload.dir:/uploads}")
    private String uploadDir;

    private final BoardRepository boardRepo;
    private final UserRepository userRepo;

    public BoardFileController(BoardRepository boardRepo, UserRepository userRepo) {
        this.boardRepo = boardRepo;
        this.userRepo = userRepo;
    }

    /**
     * 1) 파일 업로드 (작성 중)
     * POST /api/board/{boardId}/file
     * Body: multipart/form-data, fieldName="file"
     */
    @PostMapping("/{boardId}/file")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long boardId,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        // 1. 로그인 사용자 확인
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Login required.");
        }
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 2. 게시글 존재 여부
        Board post = boardRepo.findById(boardId).orElse(null);
        if (post == null) {
            return ResponseEntity.badRequest().body("Board post not found.");
        }

        // (권한 체크) 본인만 업로드 가능 or 관리자?
        // 생략 or @PreAuthorize or if needed

        // 3. 파일 체크
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            // 4. 저장할 파일명
            String originalName = file.getOriginalFilename();
            String storedName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir, storedName);
            // 5. 실제 저장
            file.transferTo(dest);

            // 6. DB에 파일명 기록
            post.setStoredFilename(storedName);
            boardRepo.save(post);

            return ResponseEntity.ok("File uploaded: " + storedName);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("File error: " + e.getMessage());
        }
    }

    /**
     * 2) 파일 삭제 (첨부파일 제거)
     * DELETE /api/board/{boardId}/file
     */
    @DeleteMapping("/{boardId}/file")
    public ResponseEntity<?> deleteFile(
            @PathVariable Long boardId,
            Authentication auth) {
        // 1. 로그인 사용자 확인
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("Login required.");
        }
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 2. 게시글 존재 여부
        Board post = boardRepo.findById(boardId).orElse(null);
        if (post == null) {
            return ResponseEntity.badRequest().body("Board post not found.");
        }

        // (권한 체크) 본인 or 관리자?
        // ...

        // 3. 파일 삭제
        String storedName = post.getStoredFilename();
        if (storedName != null) {
            File dest = new File(uploadDir, storedName);
            if (dest.exists()) {
                boolean deleted = dest.delete();
                if (!deleted) {
                    return ResponseEntity.ok("File removal failed on disk, but DB updated.");
                }
            }
            // DB에서 파일명 제거
            post.setStoredFilename(null);
            boardRepo.save(post);
        }

        return ResponseEntity.ok("File removed from board post.");
    }
}
