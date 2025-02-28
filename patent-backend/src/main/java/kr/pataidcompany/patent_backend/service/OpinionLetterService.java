package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.OpinionLetter;
import kr.pataidcompany.patent_backend.repository.OpinionLetterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OpinionLetterService {

    @Autowired
    private OpinionLetterRepository letterRepo;

    public OpinionLetter createLetter(OpinionLetter letter) {
        return letterRepo.save(letter);
    }

    public OpinionLetter getLetter(Long id) {
        return letterRepo.findById(id).orElse(null);
    }

    public OpinionLetter updateLetter(Long id, OpinionLetter updated) {
        OpinionLetter letter = letterRepo.findById(id).orElse(null);
        if (letter == null) {
            return null;
        }
        // 필요한 필드 업데이트
        letter.setCaseNumber(updated.getCaseNumber());
        letter.setOpinionContent(updated.getOpinionContent());
        letter.setEvidence(updated.getEvidence());
        // ...
        return letterRepo.save(letter);
    }

    public boolean deleteLetter(Long id) {
        if (letterRepo.existsById(id)) {
            letterRepo.deleteById(id);
            return true;
        }
        return false;
    }
}
