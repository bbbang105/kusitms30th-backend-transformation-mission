package kusitms.backend.domain.auth.dto.response;

import kusitms.backend.domain.user.entity.Provider;

import java.util.Map;

public class NaverResponse implements OAuth2Response{

//    네이버는 아래와 같이 이중 방식 객체
//    {
//        resultcode=00, message=success, response={id=123123123, name=~~~}
//    }

    private final Map<String, Object> attribute;

    public NaverResponse(Map<String, Object> attribute) {
        this.attribute = (Map<String, Object>) attribute.get("response");
    }

    @Override
    public String getProvider() {
        return Provider.NAVER.getProviderName();
    }

    @Override
    public String getProviderId() {
        return attribute.get("id").toString();
    }

    @Override
    public String getEmail() {
        return attribute.get("email").toString();
    }

    @Override
    public String getName() {
        return attribute.get("name").toString();
    }
}
