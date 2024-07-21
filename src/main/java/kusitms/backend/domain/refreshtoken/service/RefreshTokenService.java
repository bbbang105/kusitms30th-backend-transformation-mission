package kusitms.backend.domain.refreshtoken.service;

import kusitms.backend.domain.refreshtoken.entity.RefreshToken;
import kusitms.backend.domain.refreshtoken.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Transactional
    public void saveOrUpdateToken(Long userId, String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(userId, token);
        } else {
            refreshToken.updateToken(token);
        }
    }

}
