package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.Member;

@Data
public class LoginDTO {

    private MemberInfoDTO member;
    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("exist_user")
    private boolean existUser;

    public LoginDTO(Member member, String accessToken, String refreshToken, boolean existUser) {
        this.member = new MemberInfoDTO(member);
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.existUser = existUser;
    }
}
