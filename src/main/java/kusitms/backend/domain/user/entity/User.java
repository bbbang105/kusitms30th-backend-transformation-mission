package kusitms.backend.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String provider;

    @Column(nullable = false, unique = true)
    private String providerId;

    @Builder
    public User(String name, String provider, String providerId) {
        this.name = name;
        this.provider = provider;
        this.providerId = providerId;
    }

    public void modifyUserName(String name){
        this.name=name;
    }
}
