package kr.pataidcompany.patent_backend.controller;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import kr.pataidcompany.patent_backend.model.OpinionLetter;
import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserDocumentsController {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PatentDocumentRepository patentDocRepo;
    @Autowired
    private PriorArtSearchReportRepository reportRepo;
    @Autowired
    private OpinionLetterRepository letterRepo;

    /**
     * [1] 본인이 작성한 명세서 목록
     * GET /api/user/my-documents
     */
    @GetMapping("/my-documents")
    public ResponseEntity<?> getMyPatentDocuments(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인 필요");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 사용자 세션");
        }

        // findByUserId(...) 직접 구현 or Query Method
        List<PatentDocument> docs = patentDocRepo.findByUserId(currentUser.getUserId());
        return ResponseEntity.ok(docs);
    }

    /**
     * [2] 본인이 작성한 선행기술조사 보고서 목록
     * GET /api/user/my-reports
     */
    @GetMapping("/my-reports")
    public ResponseEntity<?> getMyReports(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인 필요");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 사용자 세션");
        }

        List<PriorArtSearchReport> reports = reportRepo.findByUserId(currentUser.getUserId());
        return ResponseEntity.ok(reports);
    }

    /**
     * [3] 본인이 작성한 의견서 목록
     * GET /api/user/my-opinions
     */
    @GetMapping("/my-opinions")
    public ResponseEntity<?> getMyOpinionLetters(Authentication auth) {
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.badRequest().body("로그인 필요");
        }
        User currentUser = userRepo.findByUsername(auth.getName()).orElse(null);
        if (currentUser == null) {
            return ResponseEntity.badRequest().body("유효하지 않은 사용자 세션");
        }

        List<OpinionLetter> letters = letterRepo.findByUserId(currentUser.getUserId());
        return ResponseEntity.ok(letters);
    }
}
