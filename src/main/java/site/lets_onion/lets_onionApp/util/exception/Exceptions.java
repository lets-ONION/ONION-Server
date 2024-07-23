package site.lets_onion.lets_onionApp.util.exception;

import lombok.Getter;

@Getter
public enum Exceptions {

    /*400 BAD_REQUEST*/
    BAD_REQUEST(400, "Bad Request"),

    /*401 UNAUTHORIZED*/
    UNAUTHORIZED(401, "Unauthorized"),
    BLACKLISTED_TOKEN(401, "Blacklisted Token"),
    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_ISSUED_TIME(401, "Invalid Issued Time"),
    NOT_ACCESS_TOKEN(401, "Not Access Token"),
    TOKEN_NOT_FOUND(401, "Token Not Found"),
    EXPIRED_TOKEN(401, "Expired Token"),

    /*403 FORBIDDEN*/
    FORBIDDEN(403, "Forbidden"),

    /*404 NOT_FOUND*/
    NOT_FOUND(404, "Not Found"),

    /*405 METHOD_NOT_ALLOWED*/
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /*408 REQUEST_TIMEOUT*/
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /*500 INTERNAL_SERVER_ERROR*/
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String msg;

    Exceptions(int code, String message) {
        this.code = code;
        this.msg = message;
    }
}
