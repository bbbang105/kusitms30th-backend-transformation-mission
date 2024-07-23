package kusitms.backend.domain.onboarding.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class OnboardingRequest {
        @NotBlank(message = "닉네임은 공백이나 빈칸일 수 없습니다.")
        private String nickname;
        @NotNull(message = "나이는 Null이 될 수 없습니다.")
        private int age;
        @NotBlank(message = "직업은 공백이나 빈칸일 수 없습니다.")
        private String job;
}
