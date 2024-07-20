package kusitms.backend.domain.onboarding.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name="user_on_boarding")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOnBoarding {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String nickname;

    private int age;

    private String job;

    @Builder
    public UserOnBoarding(Long userId, String nickname, int age, String job) {
        this.userId = userId;
        this.nickname = nickname;
        this.age = age;
        this.job = job;
    }

}