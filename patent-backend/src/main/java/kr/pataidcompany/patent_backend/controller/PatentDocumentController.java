package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import kr.pataidcompany.patent_backend.service.PatentDocumentService;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository; // 로그인 사용자 -> userId 추출

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/patent")
public class PatentDocumentController {

    @Autowired
    private PatentDocumentService patentService;

    @Autowired
    private UserRepository userRepository; // UserRepository 주입 (이미 존재한다고 가정)

    // 1) 작성 API
    @PostMapping("/documents")
    public ResponseEntity<?> createPatentDocument(@RequestBody PatentDocument doc,
            Authentication auth) {
        // 실제 로그인 사용자 ID 설정
        // auth.getName() -> username
        // userRepository.findByUsername(...) -> user.getUserId()
        String currentUsername = auth.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 문서에 userId 연결
        doc.setUserId(currentUser.getUserId());

        // 생성
        PatentDocument saved = patentService.createDocument(doc);
        return ResponseEntity.ok(saved);
    }

    // 2) 조회 API
    @GetMapping("/documents/{id}")
    public ResponseEntity<?> getPatentDocument(@PathVariable Long id) {
        PatentDocument doc = patentService.getDocument(id);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doc);
    }

    // 3) 수정 API
    @PutMapping("/documents/{id}")
    public ResponseEntity<?> updatePatentDocument(@PathVariable Long id,
            @RequestBody PatentDocument updated) {
        PatentDocument doc = patentService.updateDocument(id, updated);
        if (doc == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(doc);
    }

    // 4) 삭제 API
    @DeleteMapping("/documents/{id}")
    public ResponseEntity<?> deletePatentDocument(@PathVariable Long id) {
        boolean deleted = patentService.deleteDocument(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Patent document deleted.");
    }
}
