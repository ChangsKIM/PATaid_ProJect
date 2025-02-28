package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.PriorArtSearchReport;
import kr.pataidcompany.patent_backend.repository.PriorArtSearchReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PriorArtSearchReportService {

    @Autowired
    private PriorArtSearchReportRepository reportRepo;

    public PriorArtSearchReport createReport(PriorArtSearchReport report) {
        return reportRepo.save(report);
    }

    public PriorArtSearchReport getReport(Long id) {
        return reportRepo.findById(id).orElse(null);
    }

    public PriorArtSearchReport updateReport(Long id, PriorArtSearchReport updated) {
        PriorArtSearchReport report = reportRepo.findById(id).orElse(null);
        if (report == null) {
            return null;
        }
        // 필요한 필드 업데이트
        report.setTitle(updated.getTitle());
        report.setSummary(updated.getSummary());
        report.setSearchStrategy(updated.getSearchStrategy());
        report.setSearchResult(updated.getSearchResult());
        report.setAnalysis(updated.getAnalysis());
        report.setConclusion(updated.getConclusion());
        // ...
        return reportRepo.save(report);
    }

    public boolean deleteReport(Long id) {
        if (reportRepo.existsById(id)) {
            reportRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
