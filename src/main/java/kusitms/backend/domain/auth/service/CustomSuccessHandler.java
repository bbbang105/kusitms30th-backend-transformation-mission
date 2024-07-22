package kusitms.backend.domain.auth.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.backend.domain.auth.dto.response.CustomOAuth2User;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import kusitms.backend.domain.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final OnboardingRepository userOnboardingRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customUserDetails.getId();
        String name = customUserDetails.getName();
        String provider = customUserDetails.getProvider();
        String providerId = customUserDetails.getProviderId();

        Onboarding onboarding = userOnboardingRepository.findByUserId(userId);
        if (onboarding == null) {
            // 온보딩 페이지로 리디렉션, 쿠키에 토큰 저장하지 않음
            response.sendRedirect("http://localhost:5173/onboarding?userId=" + userId);
        } else {
            // 기존 회원, 쿠키에 토큰 저장
            String accessToken = jwtUtil.createAccessToken(userId, name, provider, providerId);
            String refreshToken = jwtUtil.createRefreshToken(userId, name, provider, providerId);

            response.addCookie(createCookie("Authorization", accessToken));
            response.addCookie(createCookie("Refresh-Token", refreshToken));

            // 리프레쉬 토큰 저장
            refreshTokenService.saveOrUpdateToken(userId, refreshToken);

            response.sendRedirect("http://localhost:5173");
        }
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*60);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        return cookie;
    }

}
