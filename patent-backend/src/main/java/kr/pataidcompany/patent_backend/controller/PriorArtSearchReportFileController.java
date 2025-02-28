package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.PriorArtSearchReportRepository;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/prior-art")
public class PriorArtSearchReportFileController {

    @Value("${upload.dir:/uploads}")
    private String uploadDir;

    private final PriorArtSearchReportRepository reportRepo;
    private final UserRepository userRepo;

    public PriorArtSearchReportFileController(PriorArtSearchReportRepository reportRepo,
            UserRepository userRepo) {
        this.reportRepo = reportRepo;
        this.userRepo = userRepo;
    }

    /**
     * 업로드 (작성 중): 파일을 /uploads 에 저장, DB의 storedFilename에 기록
     */
    @PostMapping("/reports/{reportId}/file")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long reportId,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        // 로그인 사용자
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 보고서 존재 확인
        PriorArtSearchReport report = reportRepo.findById(reportId).orElse(null);
        if (report == null) {
            return ResponseEntity.badRequest().body("Report not found.");
        }

        // 파일 검사
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        // 저장
        try {
            String originalName = file.getOriginalFilename();
            String storedName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir, storedName);
            file.transferTo(dest);

            // DB에 파일명 기록
            report.setStoredFilename(storedName);
            reportRepo.save(report);

            return ResponseEntity.ok("File uploaded: " + storedName);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File error: " + e.getMessage());
        }
    }

    /**
     * 작성 완료: 파일 삭제, DB storedFilename=null
     */
    @PostMapping("/reports/{reportId}/complete")
    public ResponseEntity<?> completeReport(
            @PathVariable Long reportId,
            Authentication auth) {
        // 로그인 사용자
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        // 보고서 존재 확인
        PriorArtSearchReport report = reportRepo.findById(reportId).orElse(null);
        if (report == null) {
            return ResponseEntity.badRequest().body("Report not found.");
        }

        // 파일 삭제
        String storedName = report.getStoredFilename();
        if (storedName != null) {
            File dest = new File(uploadDir, storedName);
            if (dest.exists()) {
                boolean deleted = dest.delete();
                if (!deleted) {
                    return ResponseEntity.ok("Report completed, but file deletion failed.");
                }
            }
            report.setStoredFilename(null);
            reportRepo.save(report);
        }

        return ResponseEntity.ok("Report completed, file deleted.");
    }
}
