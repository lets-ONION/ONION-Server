package site.lets_onion.lets_onionApp.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoTokenRequestDTO {

    @JsonProperty("grant_type")
    private final String grantType = "authorization_code";
    @JsonProperty("client_id")
    private final String clientId;
    @JsonProperty("redirect_uri")
    private final String redirectUri;
    private final String code;

    public KakaoTokenRequestDTO(String clientId, String redirectUri, String code) {
        this.clientId = clientId;
        this.redirectUri = redirectUri;
        this.code = code;
    }
}
