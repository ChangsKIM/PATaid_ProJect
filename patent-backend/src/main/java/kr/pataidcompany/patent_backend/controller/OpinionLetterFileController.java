package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.OpinionLetter;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.OpinionLetterRepository;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@RestController
@RequestMapping("/api/opinion")
public class OpinionLetterFileController {

    @Value("${upload.dir:/uploads}")
    private String uploadDir;

    private final OpinionLetterRepository letterRepo;
    private final UserRepository userRepo;

    public OpinionLetterFileController(OpinionLetterRepository letterRepo, UserRepository userRepo) {
        this.letterRepo = letterRepo;
        this.userRepo = userRepo;
    }

    /**
     * 업로드 (작성 중)
     * POST /api/opinion/letters/{letterId}/file
     */
    @PostMapping("/letters/{letterId}/file")
    public ResponseEntity<?> uploadFile(
            @PathVariable Long letterId,
            @RequestParam("file") MultipartFile file,
            Authentication auth) {
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        OpinionLetter letter = letterRepo.findById(letterId).orElse(null);
        if (letter == null) {
            return ResponseEntity.badRequest().body("Opinion letter not found.");
        }

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("No file uploaded.");
        }

        try {
            String originalName = file.getOriginalFilename();
            String storedName = System.currentTimeMillis() + "_" + originalName;
            File dest = new File(uploadDir, storedName);
            file.transferTo(dest);

            letter.setStoredFilename(storedName);
            letterRepo.save(letter);

            return ResponseEntity.ok("File uploaded: " + storedName);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("File error: " + e.getMessage());
        }
    }

    /**
     * 작성 완료
     * POST /api/opinion/letters/{letterId}/complete
     */
    @PostMapping("/letters/{letterId}/complete")
    public ResponseEntity<?> completeLetter(
            @PathVariable Long letterId,
            Authentication auth) {
        String currentUsername = auth.getName();
        User currentUser = userRepo.findByUsername(currentUsername).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("Invalid user session.");
        }

        OpinionLetter letter = letterRepo.findById(letterId).orElse(null);
        if (letter == null) {
            return ResponseEntity.badRequest().body("Opinion letter not found.");
        }

        String storedName = letter.getStoredFilename();
        if (storedName != null) {
            File dest = new File(uploadDir, storedName);
            if (dest.exists()) {
                boolean deleted = dest.delete();
                if (!deleted) {
                    return ResponseEntity.ok("Letter completed, but file deletion failed.");
                }
            }
            letter.setStoredFilename(null);
            letterRepo.save(letter);
        }

        return ResponseEntity.ok("Letter completed, file deleted.");
    }
}
