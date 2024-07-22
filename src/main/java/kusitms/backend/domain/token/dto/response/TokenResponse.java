package kusitms.backend.domain.token.dto.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String accessToken;
    private String refreshToken;
}
