package kusitms.backend.global.common;

import org.springframework.stereotype.Component;

@Component
public class GenerateCookieString {

    public String generateCookie(String name, String value) {
        return name + "=" + value + "; Path=/; HttpOnly; SameSite=None; Secure";
    }

}
