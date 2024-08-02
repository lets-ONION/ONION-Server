package site.lets_onion.lets_onionApp.domain.calendar;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.member.Member;

import java.time.YearMonth;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "month_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    private YearMonth monthValue;

    @Builder
    public MonthData(Member member) {
        this.member = member;
        this.monthValue = YearMonth.now();
    }
}
