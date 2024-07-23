package kusitms.backend.domain.auth.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OAuth2UserDTO {
    private String name;
    private String provider;
    private String providerId;

    public static OAuth2UserDTO of(String name, String provider, String providerId) {
        return new OAuth2UserDTO(name, provider, providerId);
    }

}