package kusitms.backend.domain.onboarding.repository;

import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserOnBoardingRepository extends JpaRepository<UserOnBoarding, Long> {
    UserOnBoarding findByUserId(Long userId);
}
