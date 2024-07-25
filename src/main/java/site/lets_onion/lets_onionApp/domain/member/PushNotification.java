package site.lets_onion.lets_onionApp.domain.member;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable @Getter
public class PushNotification {

    private boolean friendRequest = true;

    private boolean tradeRequest = true;

    private boolean everyone = true;

    public void changeFriendRequest() {
        this.friendRequest = !this.friendRequest;
    }

    public void changeTradeRequest() {
        this.tradeRequest = !this.tradeRequest;
    }

    public void changeEveryone() {
        this.everyone = !this.everyone;
    }
}
