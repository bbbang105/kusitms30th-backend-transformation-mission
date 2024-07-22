package kusitms.backend.domain.token.controller;

import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.token.dto.response.TokenResponse;
import kusitms.backend.domain.token.entity.RefreshToken;
import kusitms.backend.domain.token.service.RefreshTokenService;
import kusitms.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class TokenController {

    private final RefreshTokenService refreshTokenService;

    @PutMapping("/token/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshAccessToken(@CookieValue("Refresh-Token") String refreshToken) {
        TokenResponse tokenResponse= refreshTokenService.refreshAccessToken(refreshToken);
        ApiResponse<TokenResponse> response = new ApiResponse<>(HttpStatus.OK,"Refresh Tokens Successfully", tokenResponse);
        return ResponseEntity.ok(response);
    }

}
