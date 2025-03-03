package kr.pataidcompany.patent_backend.scheduler;

import kr.pataidcompany.patent_backend.repository.OpinionLetterRepository;
import kr.pataidcompany.patent_backend.repository.PatentDocumentRepository;
import kr.pataidcompany.patent_backend.repository.PriorArtSearchReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CleanupScheduler {

    @Autowired
    private PatentDocumentRepository patentDocRepo;

    @Autowired
    private PriorArtSearchReportRepository reportRepo;

    @Autowired
    private OpinionLetterRepository letterRepo;

    /**
     * 매일 새벽 2시에, createdAt이 15일 지난 문서를 삭제
     * cron="0 0 2 * * ?" → 초, 분, 시, 일, 월, 요일
     * - 매일 02:00:00 실행
     */
    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanupOldDocs() {
        LocalDateTime cutoff = LocalDateTime.now().minusDays(15);

        patentDocRepo.deleteByCreatedAtBefore(cutoff);
        reportRepo.deleteByCreatedAtBefore(cutoff);
        letterRepo.deleteByCreatedAtBefore(cutoff);
    }
}
