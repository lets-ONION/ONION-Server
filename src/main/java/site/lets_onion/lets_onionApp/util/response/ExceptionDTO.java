package site.lets_onion.lets_onionApp.util.response;

import lombok.Data;

@Data
public class ExceptionDTO {

    private String message;
    private String responseCode;

    public ExceptionDTO(String message, ResponseCode responseCode) {
        this.message = message;
        this.responseCode = responseCode.getCode();
    }
}
