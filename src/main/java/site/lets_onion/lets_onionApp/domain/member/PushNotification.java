package site.lets_onion.lets_onionApp.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import site.lets_onion.lets_onionApp.util.push.PushType;

@Embeddable @Getter
public class PushNotification {

    private boolean friendRequest = true;
    private boolean friendAccept = true;
    private boolean tradeRequest = true;
    private boolean tradeAccept = true;
    private boolean wateringTime = true;


    public void updateNotification(PushType pushType) {
        switch (pushType) {
            case FRIEND_REQUEST:
                friendRequest = !friendRequest;
                break;
            case FRIEND_RESPONSE:
                friendAccept = !friendAccept;
                break;
            case TRADE_REQUEST:
                tradeRequest = !tradeRequest;
                break;
            case TRADE_RESPONSE:
                tradeAccept = !tradeAccept;
                break;
            case WATERING_TIME:
                wateringTime = !wateringTime;
                break;
        }
    }
}
