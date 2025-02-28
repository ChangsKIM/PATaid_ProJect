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

    // 필요 시 관리자 생성 로직 등 추가 가능
    // public User registerAdmin(User user) {
    // user.setMemberType("admin");
    // user.setRole("ROLE_ADMIN");
    // user.setPassword(passwordEncoder.encode(user.getPassword()));
    // return userRepository.save(user);
    // }
}
