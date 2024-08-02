package site.lets_onion.lets_onionApp.dto.onionBook;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OnionDTO {

    @JsonProperty("onion_type")
    private String onionType;

    @JsonProperty("onion_image")
    private String onionImage;

    @JsonProperty("quantity")
    private int quantity;

    @JsonProperty("can_trade")
    private boolean canTrade;

    public OnionDTO(String onionType, String onionImage, int quantity) {
        this.onionType = onionType;
        this.onionImage = onionImage;
        this.quantity = quantity;
        this.canTrade = this.quantity >= 2; //2이상이면 true, 아니면 false
    }
}
