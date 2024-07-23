package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StatusMessageDTO {

    @JsonProperty("status_message")
    private String statusMessage;

    public StatusMessageDTO(String statusMessage) {
        this.statusMessage = statusMessage;
    }
}
