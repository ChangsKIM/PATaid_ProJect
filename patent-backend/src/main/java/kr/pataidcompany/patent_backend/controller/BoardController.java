package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.Board;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import kr.pataidcompany.patent_backend.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@RequestMapping("/api/board")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserRepository userRepo;

    /**
     * 목록 (페이징)
     * 카테고리별 열람 권한
     */
    @PreAuthorize("@boardAuth.canView(#category, authentication)")
    @GetMapping("/{category}")
    public ResponseEntity<?> listByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Board> result = boardService.listByCategory(category.toUpperCase(), page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * 상세보기 (조회수 중복 방지: 쿠키 방식)
     * - @boardAuth.canView(...)로 열람 권한 체크
     */
    @PreAuthorize("@boardAuth.canView(#category, authentication)")
    @GetMapping("/{category}/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable String category,
            @PathVariable Long id,
            @CookieValue(name = "viewed_boards", required = false) String viewedCookie,
            HttpServletResponse response) {
        // 1) 쿠키 파싱 (예: "1,5,12")
        Set<String> viewedSet = new HashSet<>();
        if (viewedCookie != null && !viewedCookie.isEmpty()) {
            viewedSet.addAll(Arrays.asList(viewedCookie.split(",")));
        }

        boolean alreadyViewed = viewedSet.contains(String.valueOf(id));
        Board post;
        if (!alreadyViewed) {
            // 조회수 증가
            post = boardService.getPostAndIncreaseViews(id);
            // 쿠키에 추가
            viewedSet.add(String.valueOf(id));
        } else {
            // 그냥 가져오기
            post = boardService.getPost(id);
        }

        if (post == null) {
            return ResponseEntity.notFound().build();
        }

        // 2) 쿠키 갱신
        String newCookieVal = String.join(",", viewedSet);
        Cookie cookie = new Cookie("viewed_boards", newCookieVal);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60 * 24); // 1일
        response.addCookie(cookie);

        return ResponseEntity.ok(post);
    }

    /**
     * 작성
     */
    @PreAuthorize("@boardAuth.canCreate(#category, authentication)")
    @PostMapping("/{category}")
    public ResponseEntity<?> createPost(
            @PathVariable String category,
            @RequestBody Board board,
            Authentication auth) {
        String username = auth.getName();
        User currentUser = userRepo.findByUsername(username).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        board.setCategory(category.toUpperCase());
        board.setWriterId(currentUser.getUserId());
        Board saved = boardService.createPost(board);
        return ResponseEntity.ok(saved);
    }

    /**
     * 수정
     * 본인 or 관리자
     */
    @PreAuthorize("@boardAuth.canEditOrDelete(#category, #id, authentication)")
    @PutMapping("/{category}/{id}")
    public ResponseEntity<?> updatePost(
            @PathVariable String category,
            @PathVariable Long id,
            @RequestBody Board updated) {
        Board result = boardService.updatePost(id, updated);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    /**
     * 삭제
     * 본인 or 관리자
     */
    @PreAuthorize("@boardAuth.canEditOrDelete(#category, #id, authentication)")
    @DeleteMapping("/{category}/{id}")
    public ResponseEntity<?> deletePost(
            @PathVariable String category,
            @PathVariable Long id) {
        boolean deleted = boardService.deletePost(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Post deleted.");
    }

    /**
     * 검색 (제목/내용) - 비로그인 열람?
     * 시나리오에 따라 권한 결정
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 만약 검색도 권한 필요하면 @PreAuthorize 붙이거나 if문
        Page<Board> result = boardService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(result);
    }
}
