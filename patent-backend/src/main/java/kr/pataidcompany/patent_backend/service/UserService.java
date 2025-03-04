package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 개인 회원가입 로직
     */
    public User registerIndividual(User user) {
        // 개인 회원임을 명시
        user.setMemberType("individual");
        // 권한(ROLE) 설정
        user.setRole("ROLE_USER");
        // 비밀번호 해싱
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // DB 저장
        return userRepository.save(user);
    }

    /**
     * 기업 회원가입 로직
     */
    public User registerCorporate(User user) {
        // 기업 회원임을 명시
        user.setMemberType("corporate");
        // 권한(ROLE) 설정
        user.setRole("ROLE_CORPORATE");
        // 비밀번호 해싱
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // DB 저장
        return userRepository.save(user);
    }

    // =========================
    // 아이디 중복확인 로직
    // =========================
    public boolean isUsernameAvailable(String username) {
        // existsByUsername() = 해당 아이디가 이미 존재하면 true 반환
        // -> "사용 가능 여부"는 반대이므로 ! (NOT) 처리
        return !userRepository.existsByUsername(username);
    }

    // =========================
    // 닉네임 중복확인 로직 (필요 시 사용)
    // =========================
    public boolean isNicknameAvailable(String nickname) {
        return !userRepository.existsByNickname(nickname);
    }
}
