package kusitms.backend.domain.auth.service;

import kusitms.backend.domain.auth.dto.request.UserDTO;
import kusitms.backend.domain.auth.dto.response.CustomOAuth2User;
import kusitms.backend.domain.auth.dto.response.GoogleResponse;
import kusitms.backend.domain.auth.dto.response.NaverResponse;
import kusitms.backend.domain.auth.dto.response.OAuth2Response;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response;
        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        User existData = userRepository.findByProviderAndProviderId(oAuth2Response.getProvider(),oAuth2Response.getProviderId());
//      새롭게 회원가입
        if (existData == null) {
            User user = User.builder()
                    .name(oAuth2Response.getName())
                    .provider(oAuth2Response.getProvider())
                    .providerId(oAuth2Response.getProviderId())
                    .build();
            userRepository.save(user);
            UserDTO userDTO = UserDTO.of(oAuth2Response.getName(),oAuth2Response.getProvider(), oAuth2Response.getProviderId());
            return new CustomOAuth2User(userDTO, user.getId());
        }
        else{
//          소셜에서의 name이 수정되었을 수도 있으므로 업데이트를 해준다.
            existData.modifyUserName(oAuth2Response.getName());

            UserDTO userDTO = UserDTO.of(oAuth2Response.getName(),oAuth2Response.getProvider(), oAuth2Response.getProviderId());
            return new CustomOAuth2User(userDTO, existData.getId());
        }

    }
}
