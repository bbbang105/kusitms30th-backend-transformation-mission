package kusitms.backend.domain.onboarding.controller;

import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.service.OnboardingService;
import kusitms.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;

    @PostMapping("/api/onboarding")
    public ResponseEntity<ApiResponse<Void>> onboardUser(@RequestBody OnboardingRequest request) {
        onboardingService.onboardUser(request);
        ApiResponse<Void> response= new ApiResponse<>(HttpStatus.CREATED,"User registered successfully",null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
