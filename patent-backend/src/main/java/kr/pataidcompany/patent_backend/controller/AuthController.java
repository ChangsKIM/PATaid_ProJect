package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.config.JwtTokenProvider;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private UserRepository userRepository;

    /**
     * 로그인 API
     * POST /api/login
     * RequestBody: { "username": "...", "password": "..." }
     * Response: { "success": true/false, "message": "...", "token": "...", "nickname": "...", "role": "...", "userId": ... }
     */
    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                );

            // 인증 시도
            Authentication authentication = authManager.authenticate(authToken);

            // 인증 성공 시 JWT 발급
            String token = jwtTokenProvider.generateToken(authentication);

            // DB에서 User 엔티티 조회 -> userId, role, nickname 가져옴
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            // userId까지 함께 응답
            return new LoginResponse(
                true,
                "로그인 성공",
                token,
                user.getNickname(),
                user.getRole(),
                user.getUserId()
            );

        } catch (BadCredentialsException e) {
            return new LoginResponse(false, "로그인 실패(자격증명 오류)", null, null, null, null);
        } catch (Exception e) {
            return new LoginResponse(false, "로그인 실패: " + e.getMessage(), null, null, null, null);
        }
    }

    // ==== DTO classes ====

    public static class LoginRequest {
        private String username;
        private String password;
        // getters/setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class LoginResponse {
        private boolean success;
        private String message;
        private String token;
        private String nickname;
        private String role;
        private Long userId; // ★ 추가

        public LoginResponse() {}

        public LoginResponse(boolean success, String message,
                             String token, String nickname,
                             String role, Long userId) {
            this.success = success;
            this.message = message;
            this.token = token;
            this.nickname = nickname;
            this.role = role;
            this.userId = userId;
        }

        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public Long getUserId() { return userId; }
        public void setUserId(Long userId) { this.userId = userId; }
    }
}
