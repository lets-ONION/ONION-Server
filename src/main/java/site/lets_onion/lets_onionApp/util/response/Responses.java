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

    private int code;
    private String message;

    Responses(int code, String message){
        this.code = code;
        this.message = message;
    }
}
