package kusitms.backend.global.common;

import jakarta.servlet.http.Cookie;
import org.springframework.stereotype.Component;

@Component
public class GenerateCookie {

    public String generateCookieString(String key, String value) {
        return key + "=" + value
                + "; Max-Age=" + (60 * 60 * 24)
                + "; Path=/"
                + "; HttpOnly"
                + "; Secure";
    }

    public Cookie generateCookieObject(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60 * 60 * 24); // 1 day
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        return cookie;
    }
}

