package site.lets_onion.lets_onionApp.util.response;

import lombok.Getter;

@Getter
public enum ResponseCode {
    OK("200", "OK"),
    CREATED("201", "Created"),
    ACCEPTED("202", "Accepted"),
    NO_CONTENT("204", "No Content"),
    MOVED_PERMANENTLY("303", "Moved Permanently"),
    BAD_REQUEST("400", "Bad Request"),
    UNAUTHORIZED("401", "Unauthorized"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    METHOD_NOT_ALLOWED("405", "Method Not Allowed"),
    REQUEST_TIMEOUT("408", "Request Timeout");

    private String code;
    private String message;

    ResponseCode(String code, String message){
        this.code = code;
        this.message = message;
    }
}
