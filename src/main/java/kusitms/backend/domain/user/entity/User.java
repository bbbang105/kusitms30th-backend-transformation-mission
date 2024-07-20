package kusitms.backend.domain.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

@Entity(name="users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String provider;

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
