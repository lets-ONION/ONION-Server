package site.lets_onion.lets_onionApp.dto.push;

import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class PushNotificationDTO {

    private final boolean friendRequest;

    private final boolean tradeRequest;

    private final boolean everyone;

    public PushNotificationDTO(Member member) {
        this.friendRequest = member.getPushNotification().isFriendRequest();
        this.tradeRequest = member.getPushNotification().isTradeRequest();
        this.everyone = member.getPushNotification().isEveryone();
    }
}
