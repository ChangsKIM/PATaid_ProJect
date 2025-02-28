package kr.pataidcompany.patent_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PRIOR_ART_SEARCH_REPORT")
public class PriorArtSearchReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자(조사자) 정보
    private Long userId;

    private String title; // 보고서 제목
    @Column(length = 2000)
    private String summary; // 발명의 간단한 설명
    @Column(length = 4000)
    private String searchStrategy; // 검색 전략(키워드, DB 등)
    @Column(length = 4000)
    private String searchResult; // 검색 결과(유사 특허 리스트)
    @Column(length = 4000)
    private String analysis; // 선행기술 분석
    @Column(length = 2000)
    private String conclusion; // 평가 및 결론

    // ▼ (추가) 업로드된 파일명
    private String storedFilename;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    // getters, setters
    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSearchStrategy() {
        return searchStrategy;
    }

    public void setSearchStrategy(String searchStrategy) {
        this.searchStrategy = searchStrategy;
    }

    public String getSearchResult() {
        return searchResult;
    }

    public void setSearchResult(String searchResult) {
        this.searchResult = searchResult;
    }

    public String getAnalysis() {
        return analysis;
    }

    public void setAnalysis(String analysis) {
        this.analysis = analysis;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
