package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    @Autowired
    private UserService userService;

    @PostMapping("/individual")
    public ResponseEntity<?> registerIndividual(@RequestBody User user) {
        try {
            User savedUser = userService.registerIndividual(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }

    @PostMapping("/corporate")
    public ResponseEntity<?> registerCorporate(@RequestBody User user) {
        try {
            User savedUser = userService.registerCorporate(user);
            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("회원가입 실패: " + e.getMessage());
        }
    }
}
