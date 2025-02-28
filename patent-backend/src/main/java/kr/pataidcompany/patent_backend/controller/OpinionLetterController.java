package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.OpinionLetter;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import kr.pataidcompany.patent_backend.service.OpinionLetterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/opinion")
public class OpinionLetterController {

    @Autowired
    private OpinionLetterService letterService;

    @Autowired
    private UserRepository userRepository;

    // 1) 작성
    @PostMapping("/letters")
    public ResponseEntity<?> createLetter(@RequestBody OpinionLetter letter,
            Authentication auth) {
        // 로그인 사용자
        String currentUsername = auth.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }
        // userId 연결
        letter.setUserId(currentUser.getUserId());

        OpinionLetter saved = letterService.createLetter(letter);
        return ResponseEntity.ok(saved);
    }

    // 2) 조회
    @GetMapping("/letters/{id}")
    public ResponseEntity<?> getLetter(@PathVariable Long id) {
        OpinionLetter found = letterService.getLetter(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }

    // 3) 수정
    @PutMapping("/letters/{id}")
    public ResponseEntity<?> updateLetter(@PathVariable Long id,
            @RequestBody OpinionLetter updated) {
        OpinionLetter result = letterService.updateLetter(id, updated);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // 4) 삭제
    @DeleteMapping("/letters/{id}")
    public ResponseEntity<?> deleteLetter(@PathVariable Long id) {
        boolean deleted = letterService.deleteLetter(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Opinion letter deleted.");
    }
}
