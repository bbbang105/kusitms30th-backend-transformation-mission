package kusitms.backend.global.common;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class GenerateCookie {

    public Cookie generateCookieObject(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24); // 1 day
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }
}

