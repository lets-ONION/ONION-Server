package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OnionImageUrlDTO {

    @JsonProperty("onion_image")
    private String onionImage;

    public OnionImageUrlDTO(String onionImage) {
        this.onionImage = onionImage;
    }
}
