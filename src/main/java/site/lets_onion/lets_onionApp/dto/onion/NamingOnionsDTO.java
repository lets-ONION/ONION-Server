package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NamingOnionsDTO {

    @JsonProperty("onion_name")
    private String onionName;

    public NamingOnionsDTO(String onionName) {
        this.onionName = onionName;
    }
}
