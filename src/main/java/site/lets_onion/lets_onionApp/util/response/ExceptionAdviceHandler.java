package site.lets_onion.lets_onionApp.util.response;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdviceHandler {

    @ExceptionHandler({UncheckException.class})
    public ResponseEntity handleUncheckException(UncheckException e) {
        return new ResponseEntity(
                new ExceptionDTO(e.getMessage(), e.getResponseCode()),
                HttpStatus.valueOf(e.getCode()));
    }
}
