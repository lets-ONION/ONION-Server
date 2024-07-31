package site.lets_onion.lets_onionApp.dto.onion;

import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class BabyOnionsDTO {

    private PosOnionDTO posOnion;
    private NegOnionDTO negOnion;
    private boolean isSpoken;

    public BabyOnionsDTO(Member member, boolean isSpoken) {
        this.posOnion = new PosOnionDTO(member.getOnions().getPosOnion());
        this.negOnion = new NegOnionDTO(member.getOnions().getNegOnion());
        this.isSpoken = isSpoken;
    }
}
