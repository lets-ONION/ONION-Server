package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class LoginDTO {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @JsonProperty("exist_user")
    private boolean existUser;

    public LoginDTO(String accessToken, String refreshToken, boolean existUser) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.existUser = existUser;
    }
}
