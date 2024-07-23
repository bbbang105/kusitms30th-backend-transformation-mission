package kusitms.backend.domain.onboarding.service;

import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.dto.request.ModifyOnboardingInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.OnboardingInfoResponse;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.token.service.RefreshTokenService;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import kusitms.backend.global.common.GenerateCookie;
import kusitms.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;
    private final GenerateCookie generateCookie;

    @Transactional
    public void onboardUser(Long userId, OnboardingRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND,"User not found"));

        Onboarding onboarding = Onboarding.builder()
                .user(user)
                .nickname(request.getNickname())
                .age(request.getAge())
                .job(request.getJob())
                .build();
        onboardingRepository.save(onboarding);

//        토큰 생성 및 쿠키 설정
        String accessToken = jwtUtil.generateAccessToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", generateCookie.generateCookieString("Access-Token", accessToken));
        headers.add("Set-Cookie", generateCookie.generateCookieString("Refresh-Token", refreshToken));
        headers.setLocation(URI.create("http://localhost:5173"));

//        리프레쉬 토큰 저장
        refreshTokenService.saveOrUpdateToken(user.getId(), refreshToken);
    }

    @Transactional(readOnly = true)
    public OnboardingInfoResponse getOnboardingInfo(Long userId) {
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Onboarding not found"));
        return OnboardingInfoResponse.from(onboarding);
    }

    @Transactional
    public void modifyOnboardingInfo(Long userId, ModifyOnboardingInfoRequest request) {
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Onboarding not found"));
        onboarding.modifyOnboarding(request);
    }

}
