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
     */
    @PreAuthorize("@boardAuth.canView(#category.toUpperCase(), authentication)")
    @GetMapping("/{category}")
    public ResponseEntity<?> listByCategory(
            @PathVariable String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // category 대문자화
        Page<Board> result = boardService.listByCategory(category.toUpperCase(), page, size);
        return ResponseEntity.ok(result);
    }

    /**
     * 상세보기 (조회수 중복 방지: 쿠키 방식)
     * - category 대소문자 불일치 방지
     */
    @PreAuthorize("@boardAuth.canView(#cat.toUpperCase(), authentication)")
    @GetMapping("/{category}/{id}")
    public ResponseEntity<?> getPost(
            @PathVariable("category") String cat,
            @PathVariable Long id,
            @CookieValue(name = "viewed_boards", required = false) String viewedCookie,
            HttpServletResponse response) {

        // category를 대문자로 통일
        String category = cat.toUpperCase();

        // 조회수 중복 방지용 쿠키 파싱
        Set<String> viewedSet = new HashSet<>();
        if (viewedCookie != null && !viewedCookie.isEmpty()) {
            viewedSet.addAll(Arrays.asList(viewedCookie.split(",")));
        }

        boolean alreadyViewed = viewedSet.contains(String.valueOf(id));
        Board post;
        if (!alreadyViewed) {
            // 조회수 증가
            post = boardService.getPostAndIncreaseViews(id);
            viewedSet.add(String.valueOf(id));
        } else {
            // 조회수 증가 없이 그냥 가져오기
            post = boardService.getPost(id);
        }

        if (post == null) {
            // 게시글이 없으면 404
            return ResponseEntity.notFound().build();
        }

        // 새로 본 글이라면 쿠키 갱신
        if (!alreadyViewed) {
            String newCookieVal = String.join(",", viewedSet);
            Cookie cookie = new Cookie("viewed_boards", newCookieVal);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60 * 24); // 1일
            response.addCookie(cookie);
        }

        // JSON 형태로 post 반환
        return ResponseEntity.ok(post);
    }

    /**
     * 작성
     */
    @PreAuthorize("@boardAuth.canCreate(#category.toUpperCase(), authentication)")
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

        // category 대문자화
        board.setCategory(category.toUpperCase());
        board.setWriterId(currentUser.getUserId());
        Board saved = boardService.createPost(board);

        // 글 작성 후, DB에 저장된 Board 엔티티를 JSON으로 반환
        return ResponseEntity.ok(saved);
    }

    /**
     * 수정
     */
    @PreAuthorize("@boardAuth.canEditOrDelete(#category.toUpperCase(), #id, authentication)")
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
     */
    @PreAuthorize("@boardAuth.canEditOrDelete(#category.toUpperCase(), #id, authentication)")
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
     * 검색 (제목/내용)
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchPosts(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        // 권한 필요 시 @PreAuthorize
        Page<Board> result = boardService.searchPosts(keyword, page, size);
        return ResponseEntity.ok(result);
    }
}
