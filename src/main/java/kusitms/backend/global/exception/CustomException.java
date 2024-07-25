package kusitms.backend.global.exception;

import kusitms.backend.global.common.ApiResponseCode;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ApiResponseCode responseCode;

    public CustomException(ApiResponseCode responseCode) {
        super(responseCode.getMessage());
        this.responseCode = responseCode;
    }
}