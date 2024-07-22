package kusitms.backend.domain.onboarding.controller;

import jakarta.servlet.http.Cookie;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.dto.request.UserOnBoardingRequest;
import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import kusitms.backend.domain.onboarding.repository.UserOnBoardingRepository;
import kusitms.backend.domain.refreshtoken.service.RefreshTokenService;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

//추후 분리 예정
@RestController
@RequiredArgsConstructor
public class OnboardingController {
    private final UserRepository userRepository;
    private final UserOnBoardingRepository userOnBoardingRepository;
    private final JWTUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/api/onboarding")
    public ResponseEntity<Void> onboardUser(@RequestBody UserOnBoardingRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserOnBoarding userOnBoarding = UserOnBoarding.builder()
                .user(user)
                .nickname(request.getNickname())
                .age(request.getAge())
                .job(request.getJob())
                .build();
        userOnBoardingRepository.save(userOnBoarding);

        // 토큰 생성 및 쿠키 설정
        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId());

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", createCookie("Authorization", accessToken));
        headers.add("Set-Cookie", createCookie("Refresh-Token", refreshToken));
        headers.setLocation(URI.create("http://localhost:5173"));

        // 리프레쉬 토큰 저장
        refreshTokenService.saveOrUpdateToken(user.getId(), refreshToken);

        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    private String createCookie(String key, String value) {
        return key + "=" + value + "; Max-Age=3600; Path=/; HttpOnly";
    }
}
