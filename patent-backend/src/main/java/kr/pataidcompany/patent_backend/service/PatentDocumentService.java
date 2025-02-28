package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.PatentDocument;
import kr.pataidcompany.patent_backend.repository.PatentDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PatentDocumentService {

    @Autowired
    private PatentDocumentRepository patentDocRepo;

    public PatentDocument createDocument(PatentDocument doc) {
        return patentDocRepo.save(doc);
    }

    public PatentDocument getDocument(Long id) {
        return patentDocRepo.findById(id).orElse(null);
    }

    public PatentDocument updateDocument(Long id, PatentDocument updated) {
        PatentDocument doc = patentDocRepo.findById(id).orElse(null);
        if (doc == null) {
            return null;
        }
        // 필요한 필드만 업데이트
        doc.setInventionTitle(updated.getInventionTitle());
        doc.setTechnicalField(updated.getTechnicalField());
        doc.setBackgroundTechnology(updated.getBackgroundTechnology());
        doc.setClaims(updated.getClaims());
        doc.setEffect(updated.getEffect());
        doc.setDrawingsDescription(updated.getDrawingsDescription());
        doc.setDetailedDescription(updated.getDetailedDescription());
        // ...
        return patentDocRepo.save(doc);
    }

    public boolean deleteDocument(Long id) {
        if (patentDocRepo.existsById(id)) {
            patentDocRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
