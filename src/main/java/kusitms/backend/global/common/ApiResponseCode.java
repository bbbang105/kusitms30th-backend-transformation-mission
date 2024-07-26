package kusitms.backend.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApiResponseCode {
    /**
     * 200 OK
     */
    OK(HttpStatus.OK,"요청이 성공했습니다."),

    /**
     * 201 CREATED
     */
    CREATED(HttpStatus.CREATED,"리소스 생성이 완료되었습니다."),

    /**
     * 400 BAD REQUEST
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "입력 값이 유효하지 않습니다."),

    /**
     * 401 UNAUTHORIZED
     */
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "권한이 없습니다."),

    /**
     * 403 FORBIDDEN
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근이 거부되었습니다."),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),

    /**
     * 500 INTERNAL SERVER ERROR
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
