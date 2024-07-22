package kusitms.backend.domain.token.repository;

import kusitms.backend.domain.token.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
    boolean existsByToken(String refreshToken);
}
