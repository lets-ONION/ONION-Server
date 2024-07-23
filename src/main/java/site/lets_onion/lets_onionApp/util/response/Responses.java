package site.lets_onion.lets_onionApp.util.response;

import lombok.Getter;

@Getter
public enum Responses {
    OK(200, "OK"),
    CREATED(201, "Created"),
    ACCEPTED(202, "Accepted"),
    NO_CONTENT(204, "No Content"),
    MOVED_PERMANENTLY(303, "Moved Permanently"),
    ;

    private final int code;
    private final String msg;

    Responses(int code, String msg){
        this.code = code;
        this.msg = msg;
    }
}
