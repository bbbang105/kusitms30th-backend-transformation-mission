package kusitms.backend.domain.auth.dto.response;

import kusitms.backend.domain.user.entity.Provider;

import java.util.Map;

public class GoogleResponse implements OAuth2Response{

    //    구글 반환 객체
//    {
//        resultcode=00, message=success, sub=123123123, name=~~~~
//    }

    private final Map<String, Object> attribute;

    public GoogleResponse(Map<String, Object> attribute) {
        this.attribute = attribute;
    }

    @Override
    public String getProvider() {
        return Provider.GOOGLE.getProviderName();
    }

    @Override
    public String getProviderId() {
        return attribute.get("sub").toString();
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
