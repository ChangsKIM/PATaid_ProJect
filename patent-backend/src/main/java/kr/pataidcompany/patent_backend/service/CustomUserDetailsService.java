package kr.pataidcompany.patent_backend.service;

import kr.pataidcompany.patent_backend.model.User;
import kr.pataidcompany.patent_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Spring Security가 username으로 DB에서 사용자 찾을 때 호출
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // username으로 User 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // DB에 저장된 role (ex: "ROLE_USER", "ROLE_CORPORATE", "ROLE_ADMIN")
        String role = user.getRole();
        if (role == null || role.isEmpty()) {
            role = "ROLE_USER"; // 기본값
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), // principal
                user.getPassword(), // bcrypt 해시
                Collections.singletonList(new SimpleGrantedAuthority(role)));
    }
}
