package site.lets_onion.lets_onionApp.dto.trade;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

//요청 DTO
@Data
public class TradeRequestDTO {

    @JsonProperty("req_onion")
    private String requestOnion;

    @JsonProperty("res_onion")
    private String responseOnion;

    public TradeRequestDTO(String requestOnion, String responseOnion) {
        this.requestOnion = requestOnion;
        this.responseOnion = responseOnion;
    }
}
