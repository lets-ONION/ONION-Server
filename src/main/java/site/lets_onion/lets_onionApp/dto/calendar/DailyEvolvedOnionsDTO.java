package site.lets_onion.lets_onionApp.dto.calendar;

import lombok.Data;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyEvolvedOnionsDTO {

    private LocalDate date;
    private List<OnionTypeDTO> onions;

    public DailyEvolvedOnionsDTO(DayData dayData, List<OnionTypeDTO> onions) {
        this.date = dayData.getDayValue();
        this.onions = onions;
    }
}
