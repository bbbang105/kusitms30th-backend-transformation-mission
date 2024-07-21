package kusitms.backend.domain.refreshtoken.repository;

import kusitms.backend.domain.refreshtoken.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    RefreshToken findByUserId(Long userId);
}
