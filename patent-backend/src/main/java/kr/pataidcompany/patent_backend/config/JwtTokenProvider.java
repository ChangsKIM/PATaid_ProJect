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

    public String generateToken(Authentication authentication) {
        // principal이 Spring Security의 UserDetails라고 가정
        User principal = (User) authentication.getPrincipal();
        String username = principal.getUsername();

        // HMAC-SHA용 시크릿 키 생성
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityInMs);

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public String getUsernameFromToken(String token) {
        Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token)
                .getBody().getSubject();
    }
}
