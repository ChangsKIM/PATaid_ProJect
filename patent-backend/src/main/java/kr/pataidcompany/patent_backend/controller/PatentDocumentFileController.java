package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.PatentDocumentRepository;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/patent")
public class PatentDocumentFileController {

    @Value("${upload.dir:/uploads}")
    private String uploadDir;

    private final PatentDocumentRepository docRepo;
    private final UserRepository userRepo;

    public PatentDocumentFileController(PatentDocumentRepository docRepo, UserRepository userRepo) {
        this.docRepo = docRepo;
        this.userRepo = userRepo;
    }

    /**
     * 업로드 (작성 중)
     * POST /api/patent/documents/{docId}/file
     */
    @PostMapping("/documents/{docId}/file")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long docId,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        PatentDocument doc = docRepo.findById(docId).orElse(null);
        if (doc == null) {
            return ResponseEntity.badRequest().body("Patent document not found.");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            String originalName = file.getOriginalFilename();
            String storedName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir, storedName);
            file.transferTo(dest);

            doc.setStoredFilename(storedName);
            docRepo.save(doc);

            return ResponseEntity.ok("File uploaded: " + storedName);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File error: " + e.getMessage());
        }
    }

    /**
     * 작성 완료
     * POST /api/patent/documents/{docId}/complete
     */
    @PostMapping("/documents/{docId}/complete")
    public ResponseEntity<?> completeDocument(
            @PathVariable Long docId,
            Authentication auth) {
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        PatentDocument doc = docRepo.findById(docId).orElse(null);
        if (doc == null) {
            return ResponseEntity.badRequest().body("Patent document not found.");
        }

        String storedName = doc.getStoredFilename();
        if (storedName != null) {
            File dest = new File(uploadDir, storedName);
            if (dest.exists()) {
                boolean deleted = dest.delete();
                if (!deleted) {
                    return ResponseEntity.ok("Document completed, but file deletion failed.");
                }
            }
            doc.setStoredFilename(null);
            docRepo.save(doc);
        }

        return ResponseEntity.ok("Document completed, file deleted.");
    }
}
