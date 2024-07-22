package kusitms.backend.domain.user.service;

import com.nimbusds.openid.connect.sdk.UserInfoResponse;
import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import kusitms.backend.domain.onboarding.repository.UserOnBoardingRepository;
import kusitms.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserOnBoardingRepository userOnBoardingRepository;

    public UserInfoResponse getUserInfo(Long userId) {
        UserOnBoarding userOnBoarding=userOnBoardingRepository.findByUserId(userId);
        return new UserInfoResponse(userOnBoarding.getNickname(),userOnBoarding.getAge(),userOnBoarding.getJob());
    }
}
