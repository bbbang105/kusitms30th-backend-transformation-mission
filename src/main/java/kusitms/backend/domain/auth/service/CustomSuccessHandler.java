package kusitms.backend.domain.auth.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kusitms.backend.domain.auth.dto.response.CustomOAuth2User;
import kusitms.backend.domain.auth.jwt.JWTUtil;
import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import kusitms.backend.domain.onboarding.repository.UserOnBoardingRepository;
import kusitms.backend.domain.refreshtoken.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

@Service
@RequiredArgsConstructor
public class CustomSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JWTUtil jwtUtil;
    private final UserOnBoardingRepository userOnboardingRepository;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        //OAuth2User
        CustomOAuth2User customUserDetails = (CustomOAuth2User) authentication.getPrincipal();

        Long userId = customUserDetails.getId();
        String name = customUserDetails.getName();
        String provider = customUserDetails.getProvider();
        String providerId = customUserDetails.getProviderId();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        String accessToken = jwtUtil.createAccessToken(userId,name,provider,providerId);
        String refreshToken = jwtUtil.createRefreshToken(userId,name,provider,providerId);

        response.addCookie(createCookie("Authorization", accessToken));
        response.addCookie(createCookie("Refresh-Token", refreshToken));

        // 리프레쉬 토큰 저장
        refreshTokenService.saveOrUpdateToken(userId, refreshToken);

        UserOnBoarding onboarding = userOnboardingRepository.findByUserId(userId);
//        클라이언트에 userId를 어떻게 넘겨줄 수 있을지? 온보딩 시
        if (onboarding == null) {
            response.sendRedirect("http://localhost:5173/onboarding?userId=" + userId);
        } else {
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
