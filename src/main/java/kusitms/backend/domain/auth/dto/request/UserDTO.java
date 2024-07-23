package kusitms.backend.domain.auth.dto.request;

import kusitms.backend.domain.auth.dto.response.OAuth2Response;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String provider;
    private String providerId;

    public static UserDTO of(String name, String provider, String providerId) {
        return new UserDTO(name, provider, providerId);
    }

}