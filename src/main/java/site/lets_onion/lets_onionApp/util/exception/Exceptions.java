package site.lets_onion.lets_onionApp.util.exception;

import lombok.Getter;

@Getter
public enum Exceptions {

    /*400 BAD_REQUEST*/
    BAD_REQUEST(400, "Bad Request"),
    ALREADY_REGISTERED(400, "Already Registered"),
    ONLY_REQUESTED_MEMBER_CAN_RESPONSE(400, "Only Requested Member Can Response"),
    NOT_FRIENDS_ALREADY(400, "Not Friends Already"),
    NOT_VALID_ONION_LEVEL(400, "Onion Level is not in range(0, 7)."),

    /*401 UNAUTHORIZED*/
    KAKAO_AUTH_FAILED_WITH_TOKEN(401, "Kakao Auth Failed With Token"),
    KAKAO_AUTH_FAILED_WITH_ID(401, "Kakao Auth Failed With Id"),
    UNAUTHORIZED(401, "Unauthorized"),
    BLACKLISTED_TOKEN(401, "Blacklisted Token"),
    INVALID_TOKEN(401, "Invalid Token"),
    INVALID_ISSUED_TIME(401, "Invalid Issued Time"),
    NOT_ACCESS_TOKEN(401, "Not Access Token"),
    TOKEN_NOT_FOUND(401, "Token Not Found"),
    EXPIRED_TOKEN(401, "Expired Token"),
    PREMATURE_TOKEN(401, "Premature Token"),

    /*403 FORBIDDEN*/
    FORBIDDEN(403, "Forbidden"),

    /*404 NOT_FOUND*/
    NOT_FOUND(404, "Not Found"),
    MEMBER_NOT_EXIST(404, "Member Not Exist"),
    FRIENDSHIP_NOT_EXIST(404, "Friendship Not Exist"),
    MONTH_NOT_EXIST(404, "Month Not Exist"),
    DAY_NOT_EXIST(404, "Day Not Exist"),
    GROWING_ONION_NOT_EXIST(404, "GrowingOnion Not Exist"),
    KAKAO_TOKEN_NOT_FOUND(404, """
            Kakao Token Not Found.
            Maybe Expired.
            """),

    /*405 METHOD_NOT_ALLOWED*/
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),

    /*408 REQUEST_TIMEOUT*/
    REQUEST_TIMEOUT(408, "Request Timeout"),

    /*500 INTERNAL_SERVER_ERROR*/
    FAILED_TO_SEND_PUSH_MESSAGE(500, "Failed To Send Push Message"),
    KAKAO_LOGOUT_FAILED(500, """
            Kakao Logout Failed.
            However, The Service Was Successfully Logged Out.
            """),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String msg;

    Exceptions(int code, String message) {
        this.code = code;
        this.msg = message;
    }
}
