package kusitms.backend.domain.onboarding.controller;

import kusitms.backend.domain.onboarding.dto.request.ModifyOnboardingInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.OnboardingInfoResponse;
import kusitms.backend.domain.onboarding.service.OnboardingService;
import kusitms.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class OnboardingController {
    private final OnboardingService onboardingService;

    @PostMapping("/users/{userId}/onboarding")
    public ResponseEntity<ApiResponse<Void>> onboardUser(@PathVariable Long userId, @RequestBody OnboardingRequest request) {
        onboardingService.onboardUser(userId, request);
        ApiResponse<Void> response= new ApiResponse<>(HttpStatus.CREATED,"User registered successfully",null);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<OnboardingInfoResponse>> getOnboardingInfo(@PathVariable Long userId) {
        OnboardingInfoResponse onboardingInfoResponse = onboardingService.getOnboardingInfo(userId);
        ApiResponse<OnboardingInfoResponse> response = new ApiResponse<>(HttpStatus.OK,"Get Onboarding Info Successfully",onboardingInfoResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> modifyOnboardingInfo(@PathVariable Long userId, @RequestBody ModifyOnboardingInfoRequest request) {
        onboardingService.modifyOnboardingInfo(userId, request);
        ApiResponse<Void> response= new ApiResponse<>(HttpStatus.OK,"Modify Onboarding Info Successfully",null);
        return ResponseEntity.ok(response);
    }
}
