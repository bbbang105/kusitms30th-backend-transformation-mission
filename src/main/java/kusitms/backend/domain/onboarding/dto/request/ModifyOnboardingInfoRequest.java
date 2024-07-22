package kusitms.backend.domain.onboarding.dto.request;

import lombok.Getter;

@Getter
public class ModifyOnboardingInfoRequest {
    private String username;
    private int age;
    private String job;
}
