package kusitms.backend.domain.onboarding.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kusitms.backend.domain.onboarding.dto.request.ModifyOnboardingInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.OnboardingInfoResponse;
import kusitms.backend.domain.onboarding.service.OnboardingService;
import kusitms.backend.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "onboarding", description = "온보딩 관련 api")
public class OnboardingController {
    private final OnboardingService onboardingService;

    @Operation(summary = "온보딩 정보 추가 입력", description = """
            
            userId와 추가정보인 OnboardingRequest를 이용하여 온보딩 추가정보를 입력한다.
            
            쿠키, 헤더 토큰 인증 필요없습니다.
            
            """)
    @PostMapping("/users/{userId}/onboarding")
    public ResponseEntity<ApiResponse<Void>> onboardUser(@PathVariable Long userId, @Valid @RequestBody OnboardingRequest request) {
        onboardingService.onboardUser(userId, request);
        return ApiResponse.created(null);
    }


    @Operation(summary = "온보딩 정보 조회", description = """
            
            userId를 통하여 해당 회원 온보딩 정보인 닉네임, 나이, 직업을 조회한다.
            
            헤더에 어세스토큰 인증이 필요합니다.
            
            """)
    @GetMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<OnboardingInfoResponse>> getOnboardingInfo(@PathVariable Long userId) {
        OnboardingInfoResponse onboardingInfoResponse = onboardingService.getOnboardingInfo(userId);
        return ApiResponse.ok(onboardingInfoResponse);
    }

    @Operation(summary = "온보딩 정보 조회", description = """
            
            userId와 ModifyOnboardingInfoRequest를 통하여 해당 회원 온보딩 정보인 닉네임, 나이, 직업을 수정합니다.
            
            헤더에 어세스토큰 인증이 필요합니다.
            
            """)
    @PutMapping("/users/{userId}")
    public ResponseEntity<ApiResponse<Void>> modifyOnboardingInfo(@PathVariable Long userId, @Valid @RequestBody ModifyOnboardingInfoRequest request) {
        onboardingService.modifyOnboardingInfo(userId, request);
        return ApiResponse.ok(null);
    }
}
