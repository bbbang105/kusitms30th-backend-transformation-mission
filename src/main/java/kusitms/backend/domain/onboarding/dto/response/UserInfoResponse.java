package kusitms.backend.domain.onboarding.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponse {
    private String username;
    private int age;
    private String job;
}
