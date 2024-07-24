package kusitms.backend.domain.token.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kusitms.backend.domain.token.dto.response.TokenResponse;
import kusitms.backend.domain.token.service.TokenService;
import kusitms.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Tag(name = "refresh_token", description = "토큰 관련 api")
public class TokenController {

    private final TokenService tokenService;

    @Operation(summary = "토큰 재발급", description = """
            
            리프레쉬 토큰을 이용하여 어세스토큰을 재발급합니다. (보안을 위하여 어세스토큰 재발급 시 리프레쉬토큰도 재발급)
            
            쿠키에 리프레쉬토큰 기입이 필요합니다.
            
            """)
    @PutMapping("/token/refresh")
    public ResponseEntity<ApiResponse<TokenResponse>> refreshAccessToken(@CookieValue("Refresh-Token") String refreshToken) {
        TokenResponse tokenResponse= tokenService.refreshAccessToken(refreshToken);
        ApiResponse<TokenResponse> response = new ApiResponse<>(HttpStatus.OK,"Refresh Tokens Successfully", tokenResponse);
        return ResponseEntity.ok(response);
    }

}
