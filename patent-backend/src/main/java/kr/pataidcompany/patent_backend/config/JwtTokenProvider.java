package kr.pataidcompany.patent_backend.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    // 예시: 최소 32바이트 문자열(256비트)
    // application.properties나 환경변수에서:
    // jwt.secret=my-super-secret-jwt-key-which-is-32byte
    @Value("${jwt.secret:my-super-secret-jwt-key-which-is-32byte}")
    private String secretKey;

    // 토큰 유효기간 (1시간 예시)
    private final long validityInMs = 3600000;

    // (추가) 서버 시작 시각 (재시작할 때마다 새로 설정)
    private final long serverStartTime;

    // 생성자에서 서버 시작 시각 기록
    public JwtTokenProvider() {
        this.serverStartTime = System.currentTimeMillis();
    }

    /**
     * 토큰 발급
     * - principal이 Spring Security의 UserDetails(User)라고 가정
     */
    public String generateToken(Authentication authentication) {
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();

        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)        // iat(발급 시각)
                .setExpiration(expiry)   // exp(만료 시각)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰 유효성 검증
     * - 서명 검증 + 만료 검증 + 서버 재시작 이후 발급된 토큰인지 확인
     */
    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            // 서명 및 exp 검증
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims claims = claimsJws.getBody();

            // (1) 만료 시간(exp)은 parseClaimsJws 내부에서 검증
            //     만료되면 예외(JwtException) 발생

            // (2) 서버 재시작 시점과 토큰 발급 시각(iat) 비교
            Date issuedAt = claims.getIssuedAt();
            if (issuedAt == null) {
                // iat가 없으면 무효 처리
                return false;
            }
            long iatMillis = issuedAt.getTime();

            // iat가 서버 시작 시각보다 이전이면 무효
            if (iatMillis < serverStartTime) {
                return false;
            }

            // 모든 검증 통과
            return true;
        } catch (JwtException e) {
            // 서명 불일치, 만료, 변조 등
            return false;
        }
    }

    /**
     * 토큰에서 username(subject) 추출
     */
    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
