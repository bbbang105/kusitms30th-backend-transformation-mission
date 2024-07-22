package kusitms.backend.domain.onboarding.controller;

import kusitms.backend.domain.onboarding.dto.request.ModifyUserInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.UserInfoResponse;
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
    public UserInfoResponse getUserInfo(@PathVariable Long userId) {
        UserInfoResponse userInfoResponse = onboardingService.getUserInfo(userId);
        return userInfoResponse;
    }

    @PutMapping("/users/{userId}")
    public void modifyUserInfo(@PathVariable Long userId, @RequestBody ModifyUserInfoRequest modifyUserInfoRequest) {
        onboardingService.modifyUserInfo(userId, modifyUserInfoRequest);
    }
}
