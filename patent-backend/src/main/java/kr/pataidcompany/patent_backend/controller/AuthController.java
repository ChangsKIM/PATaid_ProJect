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
    private UserRepository userRepository; // (추가) DB에서 사용자 조회

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            // 1) 사용자명/비밀번호로 인증 객체 생성
            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword()
                );

            // 2) 인증 시도
            Authentication authentication = authManager.authenticate(authToken);

            // 3) 인증 성공 시 JWT 토큰 생성
            String token = jwtTokenProvider.generateToken(authentication);

            // 4) DB에서 User 엔티티 조회 → 닉네임 포함
            User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

            // 5) LoginResponse에 nickname도 담아 반환
            return new LoginResponse(true, "로그인 성공", token, user.getNickname());

        } catch (BadCredentialsException e) {
            return new LoginResponse(false, "로그인 실패(자격증명 오류)", null, null);
        } catch (Exception e) {
            return new LoginResponse(false, "로그인 실패: " + e.getMessage(), null, null);
        }
    }

    // ====== DTO classes ======

    // 로그인 요청 DTO
    public static class LoginRequest {
        private String username;
        private String password;

        // getters/setters
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }

        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    // 로그인 응답 DTO
    public static class LoginResponse {
        private boolean success;
        private String message;
        private String token;

        // (추가) 닉네임 필드
        private String nickname;

        public LoginResponse() {}

        public LoginResponse(boolean success, String message, String token, String nickname) {
            this.success = success;
            this.message = message;
            this.token = token;
            this.nickname = nickname;
        }

        // getters/setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }

        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }

        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }

        public String getNickname() { return nickname; }
        public void setNickname(String nickname) { this.nickname = nickname; }
    }
}
