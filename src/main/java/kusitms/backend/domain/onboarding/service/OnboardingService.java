package kusitms.backend.domain.onboarding.service;

import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.refreshtoken.service.RefreshTokenService;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
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
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", createCookie("Access-Token", accessToken));
        headers.add("Set-Cookie", createCookie("Refresh-Token", refreshToken));
        headers.setLocation(URI.create("http://localhost:5173"));

//        리프레쉬 토큰 저장
        refreshTokenService.saveOrUpdateToken(user.getId(), refreshToken);
    }

//    createCookie 메서드 정의
    private String createCookie(String name, String value) {
        return name + "=" + value + "; Path=/; HttpOnly; SameSite=None; Secure";
    }
}
