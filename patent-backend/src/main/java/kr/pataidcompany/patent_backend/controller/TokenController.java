package kr.pataidcompany.patent_backend.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpServletRequest;

import java.util.HashMap;
import java.util.Map;

import kr.pataidcompany.patent_backend.config.JwtTokenProvider;

@RestController
@RequestMapping("/api")
public class TokenController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/validate-token")
    public Map<String, Object> validateToken(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>();
        String bearer = request.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            String token = bearer.substring(7);
            boolean valid = jwtTokenProvider.validateToken(token);
            result.put("valid", valid);

            if (valid) {
                String username = jwtTokenProvider.getUsernameFromToken(token);
                // 닉네임 등 필요한 정보 DB 조회 가능
                result.put("nickname", username); 
            }
        } else {
            result.put("valid", false);
        }
        return result;
    }
}
