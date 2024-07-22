package kusitms.backend.domain.token.service;

import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.token.dto.response.TokenResponse;
import kusitms.backend.domain.token.entity.RefreshToken;
import kusitms.backend.domain.token.repository.RefreshTokenRepository;
import kusitms.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdateToken(Long userId, String token) {
        RefreshToken refreshToken = findByUserId(userId);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(userId, token);
            refreshTokenRepository.save(refreshToken);
        } else {
            refreshToken.updateToken(token);
        }
    }


    @Transactional
    public TokenResponse refreshAccessToken(String refreshToken) {
        if (refreshTokenRepository.existsByToken(refreshToken)){
            if (!jwtUtil.isExpired(refreshToken)) {
                Long userId = jwtUtil.getUserId(refreshToken);
                RefreshToken storedRefreshToken = findByUserId(userId);
                if (storedRefreshToken != null && storedRefreshToken.getToken().equals(refreshToken)) {
                    return getTokenResponseByRefreshToken(refreshToken);
                }
            }
            else{
                return getTokenResponseByRefreshToken(refreshToken);
            }
        }
        throw new CustomException(HttpStatus.BAD_REQUEST,"Invalid refresh token");
    }

    @Transactional
    public TokenResponse getTokenResponseByRefreshToken(String refreshToken) {
        Long userId = jwtUtil.getUserId(refreshToken);
        String name = jwtUtil.getName(refreshToken);
        String provider = jwtUtil.getProvider(refreshToken);
        String providerId = jwtUtil.getProviderId(refreshToken);

        String newAccessToken = jwtUtil.createAccessToken(userId, name, provider, providerId);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, name, provider, providerId);

        this.saveOrUpdateToken(userId, newRefreshToken);

        return TokenResponse.builder()
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .build();
    }

    @Transactional(readOnly = true)
    public RefreshToken findByUserId(Long userId) {
        return refreshTokenRepository.findByUserId(userId);
    }

}
