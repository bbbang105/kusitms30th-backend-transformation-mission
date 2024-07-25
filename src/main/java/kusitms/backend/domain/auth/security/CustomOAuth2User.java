package kusitms.backend.domain.auth.security;

import kusitms.backend.domain.auth.dto.request.OAuth2UserDTO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2UserDTO OAuth2UserDTO;
    private final Long userId;

    public CustomOAuth2User(OAuth2UserDTO OAuth2UserDTO, Long userId) {
        this.OAuth2UserDTO = OAuth2UserDTO;
        this.userId = userId;
    }

//    받은 특성들, 획일화 어려워 따로 구현
    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

//    role과 같은 권한 받기
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    public Long getId(){
        return userId;
    }

    @Override
    public String getName() {
        return OAuth2UserDTO.getName();
    }

    public String getProvider(){
        return OAuth2UserDTO.getProvider();
    }

    public String getProviderId(){
        return OAuth2UserDTO.getProviderId();
    }
}
