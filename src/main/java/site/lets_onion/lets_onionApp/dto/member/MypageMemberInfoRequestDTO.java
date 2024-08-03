package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MypageMemberInfoRequestDTO {

    private String nickname;
    @JsonProperty("user_image_url")
    private String userImageUrl;

    @JsonCreator
    public MypageMemberInfoRequestDTO(String nickname, String userImageUrl) {
        this.nickname = nickname;
        this.userImageUrl = userImageUrl;
    }
}
