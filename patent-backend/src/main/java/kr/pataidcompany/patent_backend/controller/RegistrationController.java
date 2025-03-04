package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

// (추가) 필요 시 CORS 허용
@CrossOrigin(origins = "http://localhost:3000") 
@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    // =========================
    // 1) 개인 회원가입
    // =========================
    @PostMapping("/individual")
    public ResponseEntity<?> registerIndividual(@RequestBody User user) {
        try {
            User savedUser = userService.registerIndividual(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    // =========================
    // 2) 기업 회원가입
    // =========================
    @PostMapping("/corporate")
    public ResponseEntity<?> registerCorporate(@RequestBody User user) {
        try {
            User savedUser = userService.registerCorporate(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    // =========================
    // 3) 아이디 중복확인
    // =========================
    @GetMapping("/check-username")
    public ResponseEntity<?> checkUsername(@RequestParam String username) {
        boolean available = userService.isUsernameAvailable(username);
        return ResponseEntity.ok(java.util.Map.of("available", available));
    }

    // =========================
    // 4) 닉네임 중복확인 (필요 시 사용)
    // =========================
    @GetMapping("/check-nickname")
    public ResponseEntity<?> checkNickname(@RequestParam String nickname) {
        boolean available = userService.isNicknameAvailable(nickname);
        return ResponseEntity.ok(java.util.Map.of("available", available));
    }
}
