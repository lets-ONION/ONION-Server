package site.lets_onion.lets_onionApp.dto.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class PushNotificationDTO {

    @JsonProperty("friend_request")
    private final boolean friendRequest;
    @JsonProperty("trade_request")
    private final boolean tradeRequest;
    @JsonProperty("everyone")
    private final boolean everyone;

    public PushNotificationDTO(Member member) {
        this.friendRequest = member.getPushNotification().isFriendRequest();
        this.tradeRequest = member.getPushNotification().isTradeRequest();
        this.everyone = member.getPushNotification().isEveryone();
    }
}
