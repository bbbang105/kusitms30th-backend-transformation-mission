package kusitms.backend.domain.refreshtoken.controller;

import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.refreshtoken.dto.response.TokenResponse;
import kusitms.backend.domain.refreshtoken.entity.RefreshToken;
import kusitms.backend.domain.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/token/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(@CookieValue("Refresh-Token") String refreshToken) {
        System.out.println(refreshToken);

        if (!jwtUtil.isExpired(refreshToken)) {
            Long userId = jwtUtil.getUserId(refreshToken);
            RefreshToken storedRefreshToken = refreshTokenService.findByUserId(userId);

            if (storedRefreshToken != null && storedRefreshToken.getToken().equals(refreshToken)) {
                String name = jwtUtil.getName(refreshToken);
                String provider = jwtUtil.getProvider(refreshToken);
                String providerId = jwtUtil.getProviderId(refreshToken);

                String newAccessToken = jwtUtil.createAccessToken(userId, name, provider, providerId);
                String newRefreshToken = jwtUtil.createRefreshToken(userId, name, provider, providerId);

                refreshTokenService.saveOrUpdateToken(userId, newRefreshToken);

                TokenResponse tokenResponse = new TokenResponse(newAccessToken, newRefreshToken);
                return new ResponseEntity<>(tokenResponse, HttpStatus.OK);

            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
    }

}
