package kusitms.backend.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String providerName;
}
