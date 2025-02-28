package kr.pataidcompany.patent_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PATENT_DOCUMENT")
public class PatentDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 작성자 (User) 참조: User 테이블의 PK (userId)
    private Long userId;

    // 특허 명세서 주요 항목
    private String inventionTitle; // 발명의 명칭
    private String technicalField; // 기술분야
    @Column(length = 2000)
    private String backgroundTechnology; // 발명의 배경기술
    @Column(length = 2000)
    private String claims; // 청구범위
    @Column(length = 2000)
    private String effect; // 발명의 효과
    @Column(length = 2000)
    private String drawingsDescription; // 도면의 간단한 설명
    @Column(length = 4000)
    private String detailedDescription; // 발명을 실시하기 위한 구체적인 내용

    private String storedFilename;

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    public PatentDocument() {
    }

    // ======== Getter / Setter ========

    public Long getId() {
        return id;
    }

    public String getStoredFilename() {
        return storedFilename;
    }

    public void setStoredFilename(String storedFilename) {
        this.storedFilename = storedFilename;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInventionTitle() {
        return inventionTitle;
    }

    public void setInventionTitle(String inventionTitle) {
        this.inventionTitle = inventionTitle;
    }

    public String getTechnicalField() {
        return technicalField;
    }

    public void setTechnicalField(String technicalField) {
        this.technicalField = technicalField;
    }

    public String getBackgroundTechnology() {
        return backgroundTechnology;
    }

    public void setBackgroundTechnology(String backgroundTechnology) {
        this.backgroundTechnology = backgroundTechnology;
    }

    public String getClaims() {
        return claims;
    }

    public void setClaims(String claims) {
        this.claims = claims;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getDrawingsDescription() {
        return drawingsDescription;
    }

    public void setDrawingsDescription(String drawingsDescription) {
        this.drawingsDescription = drawingsDescription;
    }

    public String getDetailedDescription() {
        return detailedDescription;
    }

    public void setDetailedDescription(String detailedDescription) {
        this.detailedDescription = detailedDescription;
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

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
