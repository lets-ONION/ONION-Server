package site.lets_onion.lets_onionApp.dto.onionBook;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class OnionBookDTO {

    @JsonProperty("onions")
    private List<OnionDTO> onions;

    @JsonProperty("status_message")
    private String statusMessage;

    public OnionBookDTO(List<OnionDTO> onions, String statusMessage) {
        this.onions = onions;
        this.statusMessage = statusMessage;
    }
}
