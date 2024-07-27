package site.lets_onion.lets_onionApp.domain;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
public class TradeRequest {

    @Id @GeneratedValue
    @Column(name = "trade_request_id")
    private Long id;

    private Long fromMemberId; //왜 Member 아니고 Long으로만 했지? 궁금

    private Long toMemberId;

    private boolean isAccepted;

    @Enumerated(EnumType.STRING)
    private OnionType fromOnion;

    @Enumerated(EnumType.STRING)
    private OnionType toOnion;

    private LocalDate requestedAt;
}
