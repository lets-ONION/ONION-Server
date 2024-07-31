package site.lets_onion.lets_onionApp.domain.onion;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BabyOnion {

    private String name;
    private boolean isPos;
    private int growthStage;
    private OnionLevel onionLevel;
    private int generation;

    @Builder
    public BabyOnion(String name, boolean isPos) {
        this.name = name;
        this.isPos = isPos;
        this.growthStage = 0;
        this.onionLevel = OnionLevel.ZERO;
        this.generation = 1;
    }

    public void updateOnionName(String name){
        this.name = name;
    }


    public void waterOnion(){
        ++this.growthStage;
        this.onionLevel = OnionLevel.fromValue(this.growthStage);
    }

    public void nextOnion(){
        this.growthStage = 0;
        this.onionLevel = OnionLevel.ZERO;
        ++this.generation;
    }

}
