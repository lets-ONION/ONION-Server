package site.lets_onion.lets_onionApp.domain.calendar;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OnionHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "onion_history_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "day_id")
    private DayData dayData;

    @Column(name = "onion_type")
    private OnionType onionType;

    @Column(name = "name_and_generation")
    private String nameAndGeneration;

    @Builder
    public OnionHistory(DayData dayData, OnionType onionType, String nameAndGeneration) {
        this.dayData = dayData;
        this.onionType = onionType;
        this.nameAndGeneration = nameAndGeneration;
    }

}
