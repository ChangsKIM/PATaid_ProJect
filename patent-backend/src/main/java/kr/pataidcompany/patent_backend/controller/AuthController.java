package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.config.JwtTokenProvider;
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

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        try {
            // 1) UsernamePasswordAuthenticationToken
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    request.getUsername(),
                    request.getPassword());

            // 2) 인증 시도
            Authentication authentication = authManager.authenticate(authToken);

            // 3) 인증 성공 시 JWT 토큰 생성
            String token = jwtTokenProvider.generateToken(authentication);

            // 4) 반환
            return new LoginResponse(true, "로그인 성공", token);

        } catch (BadCredentialsException e) {
            return new LoginResponse(false, "로그인 실패(자격증명 오류)", null);
        } catch (Exception e) {
            return new LoginResponse(false, "로그인 실패: " + e.getMessage(), null);
        }
    }

    // === DTO classes ===
    public static class LoginRequest {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    public static class LoginResponse {
        private boolean success;
        private String message;
        private String token;

        public LoginResponse() {
        }

        public LoginResponse(boolean success, String message, String token) {
            this.success = success;
            this.message = message;
            this.token = token;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
