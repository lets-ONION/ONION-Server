package site.lets_onion.lets_onionApp.util.push;

import lombok.Getter;

@Getter
public enum PushType {

    TRADING("님이 양파 교환을 요청했어요."),
    FRIEND_REQUEST("님이 친구 요청을 보냈어요."),
    ALL("전체에게 보내는 메시지");

    private final String message;

    PushType(String message) {
        this.message = message;
    }
}
