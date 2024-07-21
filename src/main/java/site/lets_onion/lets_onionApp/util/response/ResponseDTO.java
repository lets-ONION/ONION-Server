package site.lets_onion.lets_onionApp.util.response;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private String msg;
    private String code;
    private T data;

    public ResponseDTO(String msg, ResponseCode code, T data) {
        this.msg = msg;
        this.code = code.getCode();
        this.data = data;
    }
}
