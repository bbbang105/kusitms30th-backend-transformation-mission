package kusitms.backend.domain.user.repository;

import kusitms.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByProviderAndProviderId(String provider, String providerId);
}
