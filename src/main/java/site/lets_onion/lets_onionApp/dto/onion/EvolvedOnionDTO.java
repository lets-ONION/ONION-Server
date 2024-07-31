package site.lets_onion.lets_onionApp.dto.onion;

import lombok.Data;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;

@Data
public class EvolvedOnionDTO {

    private String onionName;
    private String onionType;
    private String imageUrl;

    public EvolvedOnionDTO(OnionHistory onionHistory) {
        this.onionName = onionHistory.getNameAndGeneration();
        this.onionType = onionHistory.getOnionType().getOnionName();
        this.imageUrl = onionHistory.getOnionType().getImageUrl();
    }
}
