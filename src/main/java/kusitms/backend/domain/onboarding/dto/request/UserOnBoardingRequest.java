package kusitms.backend.domain.onboarding.dto.request;

import lombok.Getter;

@Getter
public class UserOnBoardingRequest {

        private Long userId;
        private String nickname;
        private int age;
        private String job;
}
