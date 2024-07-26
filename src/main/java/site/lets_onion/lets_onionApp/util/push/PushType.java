package site.lets_onion.lets_onionApp.util.push;

import lombok.Getter;

@Getter
public enum PushType {

    TRADE_REQUEST("님이 양파 교환을 요청했어요."),
    TRADE_RESPONSE("님이 양파 교환을 수락했어요."),
    FRIEND_REQUEST("님이 친구 요청을 보냈어요."),
    FRIEND_RESPONSE("님이 친구 요청을 수락했어요."),
    ALL("오늘 양파에 물을 안 줬어요.");

    private final String message;

    PushType(String message) {
        this.message = message;
    }
}
