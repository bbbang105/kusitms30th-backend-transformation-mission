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

    public static UserDTO from(OAuth2Response oAuth2Response) {
        return new UserDTO(oAuth2Response.getName(), oAuth2Response.getProvider(), oAuth2Response.getProviderId());
    }

}