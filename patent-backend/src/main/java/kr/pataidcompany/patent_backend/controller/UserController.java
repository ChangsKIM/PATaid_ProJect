package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

/**
 * 마이페이지: 사용자 정보 조회/수정, 비밀번호 변경, 탈퇴
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 1) 사용자 정보 조회
     * - 로그인된 사용자의 username을 얻어, DB에서 User 엔티티를 찾는다.
     */
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Authentication authentication) {
        String currentUsername = authentication.getName(); // 로그인된 사용자의 username
        User user = userRepository.findByUsername(currentUsername)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 민감 정보(비밀번호 등) 제외하고 반환해도 됨
        return ResponseEntity.ok(user);
    }

    /**
     * 2) 사용자 정보 수정
     * - 닉네임, 이메일, 연락처 등 필요 항목만 업데이트
     */
    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(Authentication authentication, @RequestBody User updatedInfo) {
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 예시: 닉네임, 이메일, 주소 등 업데이트
        user.setNickname(updatedInfo.getNickname());
        user.setEmail(updatedInfo.getEmail());
        user.setCompanyContact(updatedInfo.getCompanyContact());
        user.setAddress(updatedInfo.getAddress());
        // 필요 시 더 많은 필드 업데이트

        userRepository.save(user);
        return ResponseEntity.ok("Profile updated successfully.");
    }

    /**
     * 3) 비밀번호 변경
     * - 기존 비밀번호 확인 후 새 비밀번호로 교체
     */
    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(Authentication authentication, @RequestBody ChangePasswordRequest request) {
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 3-1) 기존 비밀번호 검사
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            return ResponseEntity.badRequest().body("기존 비밀번호가 틀립니다.");
        }

        // 3-2) 새 비밀번호 검사(길이, 특수문자 등) -> Validation or 커스텀 로직
        if (!isValidPassword(request.getNewPassword())) {
            return ResponseEntity.badRequest().body("새 비밀번호가 정책에 맞지 않습니다.");
        }

        // 3-3) 새 비밀번호 저장
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        return ResponseEntity.ok("비밀번호가 변경되었습니다.");
    }

    private boolean isValidPassword(String password) {
        // 비밀번호 정책 검사 로직 (예: 길이, 특수문자 포함 등)
        // 여기서는 간단히 길이 체크만 예시
        return password != null && password.length() >= 6;
    }

    /**
     * 4) 회원 탈퇴
     * - DB에서 해당 User 레코드를 삭제 or status='탈퇴' 등으로 처리
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteUser(Authentication authentication) {
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername)
                .orElse(null);

        if (user == null) {
            return ResponseEntity.badRequest().body("User not found.");
        }

        // 실제 삭제
        userRepository.delete(user);

        return ResponseEntity.ok("User account deleted successfully.");
    }

    /**
     * 비밀번호 변경 시 필요한 DTO
     */
    public static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        // getters, setters
        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }
}
