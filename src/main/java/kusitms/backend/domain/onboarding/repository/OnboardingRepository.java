package kusitms.backend.domain.onboarding.repository;

import kusitms.backend.domain.onboarding.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    Optional<Onboarding> findByUserId(Long userId);
}
