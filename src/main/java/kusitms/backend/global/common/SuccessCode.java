package kusitms.backend.global.common;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum SuccessCode {
    /**
     * 200 OK
     */
    OK(HttpStatus.OK,"요청이 성공했습니다."),

    /**
     * 201 CREATED
     */
    CREATED(HttpStatus.CREATED,"요청이 성공했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
