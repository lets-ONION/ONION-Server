package site.lets_onion.lets_onionApp.dto.push;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class PushNotificationDTO {

    @JsonProperty("friend_request")
    private final boolean friendRequest;
    @JsonProperty("friend_accept")
    private final boolean friendAccept;
    @JsonProperty("trade_request")
    private final boolean tradeRequest;
    @JsonProperty("trade_accept")
    private final boolean tradeAccept;
    @JsonProperty("wateringTime")
    private final boolean wateringTime;

    public PushNotificationDTO(Member member) {
        this.friendRequest = member.getPushNotification().isFriendRequest();
        this.friendAccept = member.getPushNotification().isFriendAccept();
        this.tradeRequest = member.getPushNotification().isTradeRequest();
        this.tradeAccept = member.getPushNotification().isTradeAccept();
        this.wateringTime = member.getPushNotification().isWateringTime();
    }
}
