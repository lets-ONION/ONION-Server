package site.lets_onion.lets_onionApp.util.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.PrematureJwtException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import java.nio.file.AccessDeniedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
@Slf4j
public class ExceptionAdviceHandler {

    /*개발자가 직접 예외처리*/
    @ExceptionHandler({CustomException.class})
    public ResponseEntity<ExceptionDTO> handleUncheckException(CustomException e) {
        return new ResponseEntity<>(
                new ExceptionDTO(e),
                HttpStatus.valueOf(e.getCode()));
    }

    /*메소드 유효성 검사 실패*/
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<FieldError> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        return new ResponseEntity<>(e.getBindingResult().
                getFieldErrors().get(0),
                HttpStatus.BAD_REQUEST);
    }


    /*하이버네이트 유효성 검사 실패*/
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(
            ConstraintViolationException e) {
        return new ResponseEntity<>(e.getConstraintViolations().
                iterator().next().getMessage(),
                HttpStatus.BAD_REQUEST);
    }


    /*지원하지 않는 HTTP METHOD 요청*/
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<String> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.METHOD_NOT_ALLOWED);
    }


    /*지원되지 않는 미디어 타입 요청*/
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<String> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException e) {
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }


    /*데이터베이스에서 엔티티 조회 실패(없음)*/
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<String> handleEntityNotFoundException(EntityNotFoundException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
    }


    /*접근 권한 없음*/
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        return new ResponseEntity<>("Access Denied: " + e.getMessage(),
                HttpStatus.FORBIDDEN);
    }


    /*잘못된 인자를 포함한 요청*/
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(
            IllegalArgumentException e) {
        return new ResponseEntity<>("Invalid Argument: "
                + e.getMessage(), HttpStatus.BAD_REQUEST);
    }


    /*예상치 못한 예외상황*/
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException e) {
        return new ResponseEntity<>(e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /*WebClient 예외*/
    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException e) {
        log.error(e.getResponseBodyAsString());
        return new ResponseEntity<>(e.getMessage(), e.getStatusCode());
    }

    /*JJWT 예외*/
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ExceptionDTO> handleJwtException(JwtException e) {
        log.error(e.getMessage());
        Exceptions exceptions;
        if (e instanceof ExpiredJwtException) {
            exceptions = Exceptions.EXPIRED_TOKEN;
        } else if (e instanceof PrematureJwtException) {
            exceptions = Exceptions.PREMATURE_TOKEN;
        } else {
            exceptions = Exceptions.INVALID_TOKEN;
        }
        CustomException ce = new CustomException(exceptions);

        return new ResponseEntity<>(
            new ExceptionDTO(ce),
            HttpStatus.valueOf(ce.getCode())
        );
    }
}
