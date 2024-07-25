package kusitms.backend.domain.auth.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.auth.security.CustomOAuth2User;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.token.service.TokenService;
import kusitms.backend.global.common.GenerateCookie;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final OnboardingRepository onboardingRepository;
    private final TokenService tokenService;
    private final GenerateCookie generateCookie;

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customOAuth2User.getId();
        String name = customOAuth2User.getName();
        String provider = customOAuth2User.getProvider();
        String providerId = customOAuth2User.getProviderId();

        Optional<Onboarding> onboarding = onboardingRepository.findByUserId(userId);
        if (onboarding.isEmpty()) {
            // 온보딩 페이지로 리디렉션, 쿠키에 토큰 저장하지 않음
            response.sendRedirect("http://localhost:5173/onboarding?userId=" + userId);
        } else {
            // 기존 회원, 쿠키에 토큰 저장
            String accessToken = jwtUtil.generateAccessToken(userId, name, provider, providerId);
            String refreshToken = jwtUtil.generateRefreshToken(userId, name, provider, providerId);

            response.addCookie(generateCookie.generateCookieObject("Access-Token", accessToken));
            response.addCookie(generateCookie.generateCookieObject("Refresh-Token", refreshToken));

            // 리프레쉬 토큰 저장
            tokenService.saveOrUpdateToken(userId, refreshToken);

            response.sendRedirect("http://localhost:5173");
        }
    }
}
