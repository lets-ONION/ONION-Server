package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GainedOnionDTO {

    private List<OnionImageUrlDTO> onions;

    public GainedOnionDTO(List<OnionImageUrlDTO> onions) {
        this.onions = onions;
    }
}
