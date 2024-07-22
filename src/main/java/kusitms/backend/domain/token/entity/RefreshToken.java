package kusitms.backend.domain.token.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "refresh_token")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @Column(nullable = false, length = 512)
    private String token;

    @Builder
    public RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public void updateToken(String token){
        this.token = token;
    }

}
