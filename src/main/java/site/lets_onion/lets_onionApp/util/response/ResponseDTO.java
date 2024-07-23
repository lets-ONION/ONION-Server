package site.lets_onion.lets_onionApp.util.response;

import lombok.Data;

@Data
public class ResponseDTO<T> {

    private String msg;
    private int code;
    private T data;

    public ResponseDTO(T data, Responses responses) {
        this.msg = responses.getMsg();
        this.code = responses.getCode();
        this.data = data;
    }
}
