package kr.pataidcompany.patent_backend.repository;

import kr.pataidcompany.patent_backend.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 로그인 시 '아이디'로 사용하는 username 필드를 검색
     */
    Optional<User> findByUsername(String username);
}
