package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;

@Data
public class EvolvedOnionDTO {

    @JsonProperty("onion_name")
    private String onionName;
    @JsonProperty("onion_type")
    private String onionType;
    @JsonProperty("image_url")
    private String imageUrl;

    public EvolvedOnionDTO(OnionHistory onionHistory) {
        this.onionName = onionHistory.getNameAndGeneration();
        this.onionType = onionHistory.getOnionType().getOnionName();
        this.imageUrl = onionHistory.getOnionType().getImageUrl();
    }
}
