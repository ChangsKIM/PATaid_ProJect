package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import kr.pataidcompany.patent_backend.service.PriorArtSearchReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prior-art")
public class PriorArtSearchReportController {

    @Autowired
    private PriorArtSearchReportService reportService;

    @Autowired
    private UserRepository userRepository;

    // 1) 작성
    @PostMapping("/reports")
    public ResponseEntity<?> createReport(@RequestBody PriorArtSearchReport report,
            Authentication auth) {
        // 실제 로그인 사용자 ID 연결
        String currentUsername = auth.getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }
        report.setUserId(currentUser.getUserId());

        PriorArtSearchReport saved = reportService.createReport(report);
        return ResponseEntity.ok(saved);
    }

    // 2) 조회
    @GetMapping("/reports/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id) {
        PriorArtSearchReport found = reportService.getReport(id);
        if (found == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(found);
    }

    // 3) 수정
    @PutMapping("/reports/{id}")
    public ResponseEntity<?> updateReport(@PathVariable Long id,
            @RequestBody PriorArtSearchReport updated) {
        PriorArtSearchReport result = reportService.updateReport(id, updated);
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    // 4) 삭제
    @DeleteMapping("/reports/{id}")
    public ResponseEntity<?> deleteReport(@PathVariable Long id) {
        boolean deleted = reportService.deleteReport(id);
        if (!deleted) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok("Prior art search report deleted.");
    }
}
