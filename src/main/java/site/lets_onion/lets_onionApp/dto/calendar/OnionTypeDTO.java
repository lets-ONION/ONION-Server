package site.lets_onion.lets_onionApp.dto.calendar;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;

@Data
public class OnionTypeDTO {

    private String name;
    @JsonProperty("image_url")
    private String imageUrl;

    public OnionTypeDTO(OnionHistory onionHistory) {
        this.name = onionHistory.getNameAndGeneration();
        this.imageUrl = onionHistory.getOnionType().getImageUrl();
    }
}
