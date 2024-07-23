package site.lets_onion.lets_onionApp.util.exception;

import lombok.Getter;

@Getter
public enum Exceptions {

    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    BLACKLISTED_TOKEN(401, "Blacklisted Token"),
    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_ISSUED_TIME(401, "Invalid Issued Time"),
    NOT_ACCESS_TOKEN(401, "Not Access Token"),
    EXPIRED_TOKEN(401, "Expired Token"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),
    REQUEST_TIMEOUT(408, "Request Timeout"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),;

    private int code;
    private String msg;

    Exceptions(int code, String message) {
        this.code = code;
        this.msg = message;
    }
}
