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

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String generateAccessToken(Long userId, String name, String provider, String providerId) {

        return Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .claim("provider", provider)
                .claim("providerId", providerId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000)) //1시간 밀리초로 변환
                .signWith(secretKey)
                .compact();
    }

    public String generateRefreshToken(Long userId, String name, String provider, String providerId) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("name", name)
                .claim("provider", provider)
                .claim("providerId", providerId)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1209600000)) //14일 밀리초로 변환
                .signWith(secretKey)
                .compact();
    }


}
