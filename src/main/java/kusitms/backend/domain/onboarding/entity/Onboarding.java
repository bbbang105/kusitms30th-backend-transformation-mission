package kusitms.backend.domain.onboarding.entity;

import jakarta.persistence.*;
import kusitms.backend.domain.onboarding.dto.request.ModifyUserInfoRequest;
import kusitms.backend.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name="onboarding")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Onboarding {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private int age;

    @Column(nullable = false)
    private String job;

    @Builder
    public Onboarding(User user, String nickname, int age, String job) {
        this.user = user;
        this.nickname = nickname;
        this.age = age;
        this.job = job;
    }

    public void modifyOnboarding(ModifyUserInfoRequest modifyUserInfoRequest) {
        this.nickname=modifyUserInfoRequest.getUsername();
        this.age=modifyUserInfoRequest.getAge();
        this.job=modifyUserInfoRequest.getJob();
    }
}