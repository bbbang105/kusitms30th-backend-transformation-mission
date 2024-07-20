package kusitms.backend.domain.user.service;

import kusitms.backend.domain.user.dto.response.*;
import kusitms.backend.domain.user.entity.User;
import kusitms.backend.domain.user.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if (registrationId.equals("naver")) {
            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());
        }
        else if (registrationId.equals("google")) {
            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());
        }
        else {
            return null;
        }

        //리소스 서버에서 받은 유저 정보로 사용자를 특정할 providerId (플랫폼 제공 고유 ID)가 있어야 함
        User existData = userRepository.findByProviderAndProviderId(oAuth2Response.getProvider(),oAuth2Response.getProviderId());
        if (existData == null) {
            User user = new User();
            user.setName(oAuth2Response.getName());
            user.setProvider(oAuth2Response.getProvider());
            user.setProviderId(oAuth2Response.getProviderId());

            userRepository.save(user);

//            로그인 하는 부분 (추후 수정 예정)
            UserDTO userDTO = new UserDTO();
            userDTO.setName(oAuth2Response.getName());
            userDTO.setProvider(oAuth2Response.getProvider());
            userDTO.setProviderId(oAuth2Response.getProviderId());

            return new CustomOAuth2User(userDTO);
        }
        else{
            existData.setName(oAuth2Response.getName());
            userRepository.save(existData);

            UserDTO userDTO = new UserDTO();
            userDTO.setName(oAuth2Response.getName());
            userDTO.setProvider(oAuth2Response.getProvider());
            userDTO.setProviderId(oAuth2Response.getProviderId());

            return new CustomOAuth2User(userDTO);
        }







    }
}
