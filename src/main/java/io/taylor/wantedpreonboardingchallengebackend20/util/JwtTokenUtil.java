package io.taylor.wantedpreonboardingchallengebackend20.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.taylor.wantedpreonboardingchallengebackend20.dto.request.AuthenticatedMember;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {
    @Value("${spring.jwt.key}")
    private String secretKey;

    @Value("${spring.jwt.expiration}")
    private long expiration;

    private Key key;

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(long memberId, String email, String nickName) {
        Date now = new Date();
        Date expirationDate = new Date(now.getTime() + this.expiration);

        return Jwts.builder()
                .claim("memberId", memberId)
                .claim("email", email)
                .claim("nickName", nickName)
                .issuedAt(now)
                .setExpiration(expirationDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public AuthenticatedMember getMemberIdFromToken(String accessToken) {
        Claims claims = getClaimsFromToken(accessToken);
        validateTokenClaims(claims);

        try {
            Long memberId = claims.get("memberId", Long.class);
            String email = claims.get("email", String.class);
            String nickName = claims.get("nickName", String.class);

            return new AuthenticatedMember(memberId, email, nickName);
        } catch (Exception e) {
            throw new IllegalArgumentException("토큰에서 사용자 정보를 추출하는 데 실패했습니다.");
        }
    }

    private static void validateTokenClaims(Claims claims) {
        if (claims.getExpiration().before(new Date()))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "유효하지 않은 토큰입니다.");
    }

    public Claims getClaimsFromToken(String accessToken) {
        return Jwts.parser()
                .verifyWith((SecretKey) key)
                .build().parseSignedClaims(accessToken)
                .getPayload();
    }
}
