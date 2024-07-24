package kusitms.backend.global.exception;

import kusitms.backend.global.common.ApiResponse;
import kusitms.backend.global.common.ApiResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException ex) {
        ApiResponse<Void> response = ApiResponse.of(ex.getResponseCode(), null);
        return new ResponseEntity<>(response, ex.getResponseCode().getHttpStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException ex) {
        ApiResponse<Void> response = ApiResponse.of(ApiResponseCode.BAD_REQUEST, null);
        return new ResponseEntity<>(response, ApiResponseCode.BAD_REQUEST.getHttpStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception ex) {
        ApiResponse<Void> response = ApiResponse.of(ApiResponseCode.INTERNAL_SERVER_ERROR, null);
        return new ResponseEntity<>(response, ApiResponseCode.INTERNAL_SERVER_ERROR.getHttpStatus());
    }
}
