package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 관리자 권한으로 특정 사용자 강제 탈퇴(실제 DB 삭제)
     * 
     * @param id 탈퇴시킬 사용자 ID (PK)
     */
    @PreAuthorize("hasRole('ADMIN')") // 메서드 레벨 권한 검사
    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
        // DB에서 사용자 찾기
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 실제 삭제
        userRepository.delete(user);

        return ResponseEntity.ok("User forcibly deleted by admin.");
    }

    /**
     * 관리자 권한으로 특정 사용자를 탈퇴 상태로만 변경하는 예시
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/deactivate-user/{id}")
    public ResponseEntity<?> deactivateUserById(@PathVariable Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 예: User 엔티티에 status 필드가 있다고 가정
        // user.setStatus("탈퇴");
        // userRepository.save(user);

        return ResponseEntity.ok("User forcibly deactivated by admin.");
    }
}
