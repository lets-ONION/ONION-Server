package site.lets_onion.lets_onionApp.dto.member;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenRequestDTO {

    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";
    @JsonProperty("client_id")
    private final String clientId;
    @JsonProperty("redirect_uri")
    private final String redirectUri;
    private final String code;

    public TokenRequestDTO(String clientId, String redirectUri, String code) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
    }
}
