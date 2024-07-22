package kusitms.backend.domain.user.service;

import kusitms.backend.domain.onboarding.dto.request.ModifyUserInfoRequest;
import kusitms.backend.domain.onboarding.dto.response.UserInfoResponse;
import kusitms.backend.domain.onboarding.entity.Onboarding;
import kusitms.backend.domain.onboarding.repository.OnboardingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final OnboardingRepository onboardingRepository;

    @Transactional(readOnly = true)
    public UserInfoResponse getUserInfo(Long userId) {
        Onboarding onboarding = onboardingRepository.findByUserId(userId);
        if (onboarding ==null) {
            return null;
        }
        else{
            UserInfoResponse userInfoResponse =new UserInfoResponse(onboarding.getNickname(), onboarding.getAge(), onboarding.getJob());
            return userInfoResponse;

        }
    }

    @Transactional
    public void modifyUserInfo(Long userId, ModifyUserInfoRequest modifyUserInfoRequest) {
        Onboarding onboarding = onboardingRepository.findByUserId(userId);
        if (onboarding !=null) {
            onboarding.modifyOnboarding(modifyUserInfoRequest);
        }
    }
}
