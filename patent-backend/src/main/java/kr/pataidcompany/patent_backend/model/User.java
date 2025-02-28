package kr.pataidcompany.patent_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 회원 유형: individual 또는 corporate
    private String memberType;

    // 개인 회원용 필드
    private String name; // 개인 이름
    private String email; // 개인 이메일
    private String username; // 로그인 ID (웹에서 "아이디"로 사용)
    private String nickname; // 닉네임

    // 기업 회원용 필드
    private String companyName;
    private String businessRegistrationNumber;
    private String companyEmail;
    private String companyContact;

    // 주소 필드 (개인/기업 공통으로 사용할 수도 있음)
    private String address;

    // 공통 필드
    private String password;
    private LocalDateTime createdAt = LocalDateTime.now();

    // ▼ (추가) 권한 필드
    // 예: "ROLE_USER", "ROLE_CORPORATE", "ROLE_ADMIN"
    private String role;

    // 기본 생성자 (JPA 필수)
    public User() {
    }

    // Getter/Setter

    public Long getUserId() {
        return userId;
    }

    public String getMemberType() {
        return memberType;
    }

    public void setMemberType(String memberType) {
        this.memberType = memberType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBusinessRegistrationNumber() {
        return businessRegistrationNumber;
    }

    public void setBusinessRegistrationNumber(String businessRegistrationNumber) {
        this.businessRegistrationNumber = businessRegistrationNumber;
    }

    public String getCompanyEmail() {
        return companyEmail;
    }

    public void setCompanyEmail(String companyEmail) {
        this.companyEmail = companyEmail;
    }

    public String getCompanyContact() {
        return companyContact;
    }

    public void setCompanyContact(String companyContact) {
        this.companyContact = companyContact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    // ▼ (추가) role getter/setter
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
