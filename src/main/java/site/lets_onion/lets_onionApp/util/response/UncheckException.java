package site.lets_onion.lets_onionApp.util.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UncheckException extends RuntimeException {

    private final ResponseCode responseCode;

    public String getCode() {
        return responseCode.getCode();
    }

    public String getMessage() {
        return responseCode.getMessage();
    }
}
