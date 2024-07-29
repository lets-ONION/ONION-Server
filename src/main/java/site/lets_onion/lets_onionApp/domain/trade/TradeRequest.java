package site.lets_onion.lets_onionApp.domain.trade;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.member.Member;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeRequest {

    @Id @GeneratedValue
    @Column(name = "trade_request_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_member_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_member_id")
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private TradeStatus status;

    @Enumerated(EnumType.STRING)
    private OnionType fromOnion;

    @Enumerated(EnumType.STRING)
    private OnionType toOnion;

    private LocalDate requestAt;

    //생성메서드
    public static TradeRequest createTradeRequest(Member fromMember, Member toMember, OnionType fromOnionType, OnionType toOnionType) {
        TradeRequest tradeRequest = new TradeRequest();

        tradeRequest.fromMember = fromMember; //setter로 해야하나..?
        tradeRequest.toMember = toMember;
        tradeRequest.fromOnion = fromOnionType;
        tradeRequest.toOnion = toOnionType;

        tradeRequest.status = TradeStatus.PENDING; //디폴트값
        tradeRequest.requestAt = LocalDate.now();

        //fromMember의 fromOnion 개수 감소 -> TradeService에.

        return tradeRequest;

    }

    //비즈니스로직
    /**
     * 요청 취소, 수락, 거절 시 tradeRequest의 상태 변경
     */
    public void updateStatus(TradeStatus requestStatus) {
        this.status = requestStatus;
    }


}
