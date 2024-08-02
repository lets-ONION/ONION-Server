package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.onion.Onion;

@Data
public class NegOnionDTO {

    private String name;
    private int level;
    @JsonProperty("image_url")
    private String imageUrl;

    public NegOnionDTO(Onion onion) {
        this.name = onion.getName() + " " + onion.getGeneration() + "세";
        this.level = onion.getGrowthStage();
        this.imageUrl = onion.getOnionLevel().getNegImageUrl();
    }

}
