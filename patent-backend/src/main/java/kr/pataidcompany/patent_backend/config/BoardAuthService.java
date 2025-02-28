package kr.pataidcompany.patent_backend.config;

import kr.pataidcompany.patent_backend.model.Board;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.BoardRepository;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("boardAuth")
public class BoardAuthService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private BoardRepository boardRepo;

    /**
     * 시나리오:
     * - NOTICE: 관리자만 작성, 관리자/회원만 열람
     * - QUESTION: 회원(로그인) 작성, 관리자/회원 열람
     * - FREE: 회원(로그인) 작성, 비로그인 열람
     */

    // 작성 권한
    public boolean canCreate(String category, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false; // 비로그인 거부
        }
        User user = getUser(auth);
        if (user == null)
            return false;

        String role = user.getRole(); // e.g. "ROLE_ADMIN", "ROLE_USER"

        switch (category.toUpperCase()) {
            case "NOTICE":
                // 관리자만
                return "ROLE_ADMIN".equals(role);

            case "QUESTION":
            case "FREE":
                // 회원(로그인)
                return role.startsWith("ROLE_");

            default:
                return false;
        }
    }

    // 열람 권한
    public boolean canView(String category, Authentication auth) {
        switch (category.toUpperCase()) {
            case "NOTICE":
            case "QUESTION":
                // 관리자/회원만
                if (auth != null && auth.isAuthenticated()) {
                    return true;
                } else {
                    return false;
                }
            case "FREE":
                // 비로그인도 열람 가능
                return true;
            default:
                return false;
        }
    }

    // 수정/삭제 권한
    public boolean canEditOrDelete(String category, Long boardId, Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        User user = getUser(auth);
        if (user == null)
            return false;

        String role = user.getRole();
        // 관리자면 OK
        if ("ROLE_ADMIN".equals(role)) {
            return true;
        }
        // 본인 글이면 OK
        Board post = boardRepo.findById(boardId).orElse(null);
        if (post == null)
            return false;
        return post.getWriterId().equals(user.getUserId());
    }

    private User getUser(Authentication auth) {
        if (auth == null)
            return null;
        String username = auth.getName();
        return userRepo.findByUsername(username).orElse(null);
    }
}
