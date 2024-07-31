package site.lets_onion.lets_onionApp.domain.calendar;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DayData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "day_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "month_id")
    private MonthData month;

    private LocalDate dayValue;

    @Column(name = "pos_note")
    private String posNote;

    @Builder
    public DayData(MonthData month) {
        this.month = month;
        this.dayValue = LocalDate.now();
        this.posNote = null;
    }

    public void updatePosNote(String posNote){
        this.posNote = posNote;
    }

    public void deletePosNote(){
        this.posNote = null;
    }
}
