package site.lets_onion.lets_onionApp.domain.trade;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import site.lets_onion.lets_onionApp.domain.member.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class TradeRequest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @CreatedDate
    private LocalDateTime requestAt;

    @Builder
    public TradeRequest(Member fromMember, Member toMember, OnionType fromOnionType, OnionType toOnionType) {
        this.fromMember = fromMember; //setter로 해야하나..?
        this.toMember = toMember;
        this.fromOnion = fromOnionType;
        this.toOnion = toOnionType;
        this.status = TradeStatus.PENDING; //디폴트값
    }

    //비즈니스로직
    /**
     * 요청 취소, 수락, 거절 시 tradeRequest의 상태 변경
     */
    public void updateStatus(TradeStatus requestStatus) {
        this.status = requestStatus;
    }


}
