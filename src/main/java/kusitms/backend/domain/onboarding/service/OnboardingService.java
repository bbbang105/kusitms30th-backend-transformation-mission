package kusitms.backend.domain.onboarding.service;

import jakarta.servlet.http.HttpServletRequest;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.dto.request.ModifyOnboardingInfoRequest;
import kusitms.backend.domain.onboarding.dto.request.OnboardingRequest;
import kusitms.backend.domain.onboarding.dto.response.OnboardingInfoResponse;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.token.service.TokenService;
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
    private final TokenService tokenService;
    private final GenerateCookie generateCookie;
    private final HttpServletRequest httpServletRequest;

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
        String accessToken = jwtUtil.generateToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId(),3600000L); //1시간
        String refreshToken = jwtUtil.generateToken(user.getId(), user.getName(), user.getProvider(), user.getProviderId(),1209600000L); //14일

        HttpHeaders headers = new HttpHeaders();
        headers.add("Set-Cookie", generateCookie.generateCookieString("Access-Token", accessToken));
        headers.add("Set-Cookie", generateCookie.generateCookieString("Refresh-Token", refreshToken));
        headers.setLocation(URI.create("http://localhost:5173"));

//        리프레쉬 토큰 저장
        tokenService.saveOrUpdateToken(user.getId(), refreshToken);
    }

//    정적 팩토리 메소드는 객체 생성 시 사용하면 좋음(객체지향적)
//    단, 아래 마이페이지 수정과 같이 수정에서는 보통 사용하지 않음(인스턴스 메소드 사용)
    @Transactional(readOnly = true)
    public OnboardingInfoResponse getOnboardingInfo(Long userId) {
        checkMatchingTokenAndUserId(userId);
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(HttpStatus.NOT_FOUND, "Onboarding not found"));
        return OnboardingInfoResponse.from(onboarding);
    }

    @Transactional
    public void modifyOnboardingInfo(Long userId, ModifyOnboardingInfoRequest request) {
        checkMatchingTokenAndUserId(userId);
        Onboarding onboarding = onboardingRepository.findByUserId(userId)
                .orElseThrow(()->new CustomException(HttpStatus.NOT_FOUND,"Onboarding not found"));
        onboarding.modifyOnboarding(request);
    }

    @Transactional(readOnly = true)
    public void checkMatchingTokenAndUserId(Long userId) {
        String authorizationHeader = httpServletRequest.getHeader("Authorization");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            throw new CustomException(HttpStatus.UNAUTHORIZED, "Unauthorized Token");
        }

        String token = authorizationHeader.substring(7);
        Long authenticatedUserId =jwtUtil.getUserId(token);

        if (!authenticatedUserId.equals(userId)) {
            throw new CustomException(HttpStatus.FORBIDDEN, "Access denied: User ID mismatch");
        }
    }

}
