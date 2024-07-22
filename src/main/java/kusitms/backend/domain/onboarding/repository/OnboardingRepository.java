package kusitms.backend.domain.onboarding.repository;

import kusitms.backend.domain.onboarding.entity.Onboarding;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OnboardingRepository extends JpaRepository<Onboarding, Long> {
    Onboarding findByUserId(Long userId);
}
