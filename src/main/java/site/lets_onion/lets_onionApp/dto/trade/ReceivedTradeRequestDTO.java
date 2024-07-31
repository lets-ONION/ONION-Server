package site.lets_onion.lets_onionApp.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.dto.friendship.FriendDTO;

import java.time.LocalDateTime;

@Data
public class ReceivedTradeRequestDTO {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("req_member")
    private FriendDTO reqMember;

    @JsonProperty("req_onion")
    private String requestOnion;

    @JsonProperty("res_onion")
    private String responseOnion;

    @JsonProperty("trade_status")
    private TradeStatus tradeStatus;

    @JsonProperty("requested_at")
    private LocalDateTime requestedAt;

    public ReceivedTradeRequestDTO(TradeRequest tradeRequest) {
        this.id = tradeRequest.getId();
        this.reqMember = new FriendDTO(tradeRequest.getFromMember());
        this.requestOnion = tradeRequest.getFromOnion().getOnionName();
        this.responseOnion = tradeRequest.getToOnion().getOnionName();
        this.tradeStatus = tradeRequest.getStatus();
        this.requestedAt = tradeRequest.getRequestedAt();
    }
}
