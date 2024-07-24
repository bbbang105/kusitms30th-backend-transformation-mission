package kusitms.backend.global.common;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Builder
public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> of(ApiResponseCode apiResponseCode, T data){
        return ApiResponse.<T>builder()
                .status(apiResponseCode.getHttpStatus().value())
                .message(apiResponseCode.getMessage())
                .data(data)
                .build();
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data){
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.of(ApiResponseCode.OK, data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data){
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.of(ApiResponseCode.CREATED, data));
    }


}

