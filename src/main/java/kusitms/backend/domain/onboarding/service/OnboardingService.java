package kusitms.backend.domain.onboarding.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.dto.request.ModifyOnboardingInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.OnboardingInfoResponse;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.token.service.TokenService;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import kusitms.backend.global.common.ApiResponseCode;
import kusitms.backend.global.common.GenerateCookie;
import kusitms.backend.global.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OnboardingService {

    private final UserRepository userRepository;
    private final OnboardingRepository onboardingRepository;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;
    private final GenerateCookie generateCookie;
    private final HttpServletRequest httpServletRequest;

    @Transactional
    public void onboardUser(Long userId, OnboardingRequest request, HttpServletResponse response) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.NOT_FOUND));

        // 온보딩 정보 저장
        onboardingRepository.save(createOnboarding(user, request));

        // 온보딩 성공 후 로그인 화면으로 리디렉션
        response.sendRedirect("http://localhost:5173/login");
    }

    private Onboarding createOnboarding(User user, OnboardingRequest request) {
        return Onboarding.builder()
                .user(user)
                .nickname(request.getNickname())
                .age(request.getAge())
                .job(request.getJob())
                .build();
    }

//    정적 팩토리 메소드는 객체 생성 시 사용하면 좋음(객체지향적)
//    단, 아래 마이페이지 수정과 같이 수정에서는 보통 사용하지 않음(인스턴스 메소드 사용)
    @Transactional(readOnly = true)
    public OnboardingInfoResponse getOnboardingInfo(Long userId) {
        checkMatchingTokenAndUserId(userId);
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ApiResponseCode.NOT_FOUND));
        return OnboardingInfoResponse.from(onboarding);
    }

    @Transactional
    public void modifyOnboardingInfo(Long userId, ModifyOnboardingInfoRequest request) {
        checkMatchingTokenAndUserId(userId);
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(()->new CustomException(ApiResponseCode.NOT_FOUND));
        onboarding.modifyOnboarding(request);
    }

    @Transactional(readOnly = true)
    public void checkMatchingTokenAndUserId(Long userId) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(ApiResponseCode.UNAUTHORIZED);
        }

        String token = authorizationHeader.substring(7);
        Long authenticatedUserId =jwtUtil.getUserId(token);

        if (!authenticatedUserId.equals(userId)) {
            throw new CustomException(ApiResponseCode.FORBIDDEN);
        }
    }

}
