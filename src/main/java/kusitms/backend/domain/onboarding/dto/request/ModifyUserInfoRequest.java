package kusitms.backend.domain.onboarding.dto.request;

import lombok.Getter;

@Getter
public class ModifyUserInfoRequest {
    private String username;
    private int age;
    private String job;
}
