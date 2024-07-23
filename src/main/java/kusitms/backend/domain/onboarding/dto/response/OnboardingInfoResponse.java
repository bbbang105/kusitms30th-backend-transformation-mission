package kusitms.backend.domain.onboarding.dto.response;

import kusitms.backend.domain.onboarding.entity.Onboarding;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public class OnboardingInfoResponse {
    private String username;
    private int age;
    private String job;

    public static OnboardingInfoResponse from(Onboarding onboarding) {
        return new OnboardingInfoResponse(onboarding.getNickname(), onboarding.getAge(), onboarding.getJob());
    }

}
