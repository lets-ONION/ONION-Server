package site.lets_onion.lets_onionApp.dto.onion;

import lombok.Data;
import site.lets_onion.lets_onionApp.domain.onion.BabyOnion;
import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;

@Data
public class PosOnionDTO {

    private String name;
    private int level;
    private String imageUrl;
    private boolean isEvolvable;

    public PosOnionDTO(BabyOnion onion) {
        this.name = onion.getName() + " " + onion.getGeneration() + "세";
        this.level = onion.getGrowthStage();
        this.imageUrl = onion.getOnionLevel().getPosImageUrl();
        this.isEvolvable = (this.level == 7);
    }
}
