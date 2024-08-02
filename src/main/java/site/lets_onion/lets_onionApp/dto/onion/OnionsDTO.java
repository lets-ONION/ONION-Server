package site.lets_onion.lets_onionApp.dto.onion;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class OnionsDTO {

    @JsonProperty("pos_onion")
    private PosOnionDTO posOnion;
    @JsonProperty("neg_onion")
    private NegOnionDTO negOnion;
    @JsonProperty("is_spoken")
    private boolean isSpoken;

    public OnionsDTO(Member member, boolean isSpoken) {
        this.posOnion = new PosOnionDTO(member.getOnions().getPosOnion());
        this.negOnion = new NegOnionDTO(member.getOnions().getNegOnion());
        this.isSpoken = isSpoken;
    }
}
