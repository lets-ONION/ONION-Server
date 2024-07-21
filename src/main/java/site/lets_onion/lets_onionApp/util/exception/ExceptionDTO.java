package site.lets_onion.lets_onionApp.util.exception;

import lombok.Data;

@Data
public class ExceptionDTO {

    private String msg;
    private int code;

    public ExceptionDTO(String message, Exceptions exceptions) {
        this.msg = message;
        this.code = exceptions.getCode();
    }
}
