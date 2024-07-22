package kusitms.backend.domain.user.service;

import kusitms.backend.domain.onboarding.dto.request.ModifyUserInfoRequest;
import kusitms.backend.domain.onboarding.dto.response.UserInfoResponse;
import kusitms.backend.domain.onboarding.entity.UserOnBoarding;
import kusitms.backend.domain.onboarding.repository.UserOnBoardingRepository;
import kusitms.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserOnBoardingRepository userOnBoardingRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        UserOnBoarding userOnBoarding=userOnBoardingRepository.findByUserId(userId);
        if (userOnBoarding==null) {
            return null;
        }
        else{
            UserInfoResponse userInfoResponse =new UserInfoResponse(userOnBoarding.getNickname(),userOnBoarding.getAge(),userOnBoarding.getJob());
            return userInfoResponse;

        }
    }

    @Transactional
    public void modifyUserInfo(Long userId, ModifyUserInfoRequest modifyUserInfoRequest) {
        UserOnBoarding userOnBoarding=userOnBoardingRepository.findByUserId(userId);
        if (userOnBoarding!=null) {
            userOnBoarding.modifyOnboarding(modifyUserInfoRequest);
        }
    }
}
