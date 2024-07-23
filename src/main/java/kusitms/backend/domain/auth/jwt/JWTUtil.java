package kusitms.backend.domain.auth.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}")String secret) {
        secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
    }

//    JWT Parser 공통 메소드
    public Claims jwtParser(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    }

    public Long getUserId(String token) {
        return jwtParser(token).get("userId", Long.class);
    }

    public String getName(String token) {

        return jwtParser(token).get("name", String.class);
    }

    public String getProvider(String token) {

        return jwtParser(token).get("provider", String.class);
    }

    public String getProviderId(String token) {

        return jwtParser(token).get("providerId", String.class);
    }

    public Boolean isExpired(String token) {

        return jwtParser(token).getExpiration().before(new Date());
    }

//    공통 토큰 생성 메소드
    public String generateToken(Long userId, String name, String provider, String providerId, Long expiredMs ) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .claim("provider", provider)
                .claim("providerId", providerId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredMs))
                .signWith(secretKey)
                .compact();
    }


}
