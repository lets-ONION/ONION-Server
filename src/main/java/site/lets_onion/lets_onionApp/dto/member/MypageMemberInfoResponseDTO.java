package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class MypageMemberInfoResponseDTO {

    private String nickname;

    @JsonProperty("user_image_url")
    private String userImageUrl;

    public MypageMemberInfoResponseDTO(Member member) {
        this.nickname = member.getNickname();
        this.userImageUrl = member.getUserImageUrl();
    }
}
