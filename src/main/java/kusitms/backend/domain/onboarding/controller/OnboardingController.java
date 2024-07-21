package kusitms.backend.domain.onboarding.controller;

import kusitms.backend.domain.onboarding.dto.request.UserOnBoardingRequest;
import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import kusitms.backend.domain.onboarding.repository.UserOnBoardingRepository;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//추후 분리 예정
@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final UserRepository userRepository;
    private final UserOnBoardingRepository userOnBoardingRepository;

    public OnboardingController(UserRepository userRepository, UserOnBoardingRepository userOnBoardingRepository) {
        this.userRepository = userRepository;
        this.userOnBoardingRepository = userOnBoardingRepository;
    }

    @PostMapping
    public ResponseEntity<String> onboardUser(@RequestBody UserOnBoardingRequest request) {
        User user = userRepository.findById(request.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));

        UserOnBoarding userOnBoarding = UserOnBoarding.builder()
                .user(user)
                .nickname(request.getNickname())
                .age(request.getAge())
                .job(request.getJob())
                .build();

        userOnBoardingRepository.save(userOnBoarding);

        return ResponseEntity.ok("Onboarding completed");
    }
}
