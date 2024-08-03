package site.lets_onion.lets_onionApp.dto.calendar;

import lombok.Data;

import java.util.List;

@Data
public class DailyDTO {

    private List<DailyEvolvedOnionsDTO> dates;

    public DailyDTO(List<DailyEvolvedOnionsDTO> dates) {
        this.dates = dates;
    }
}
