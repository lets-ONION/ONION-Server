package site.lets_onion.lets_onionApp.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UncheckException extends RuntimeException {

    private final Exceptions exceptions;

    public int getCode() {
        return exceptions.getCode();
    }

    public String getMsg() {
        return exceptions.getMsg();
    }
}
