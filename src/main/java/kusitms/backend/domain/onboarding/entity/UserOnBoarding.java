package kusitms.backend.domain.onboarding.entity;

import jakarta.persistence.*;
import kusitms.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity(name="user_on_boarding")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserOnBoarding {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String nickname;

    private int age;

    private String job;

    @Builder
    public UserOnBoarding(User user, String nickname, int age, String job) {
        this.user = user;
        this.nickname = nickname;
        this.age = age;
        this.job = job;
    }

}