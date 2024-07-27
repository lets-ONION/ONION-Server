package site.lets_onion.lets_onionApp.dto.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Data;

@Data
public class LogoutDTO {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("refresh_token")
    private String refreshToken;
    @Nullable
    @JsonProperty("device_token")
    private String deviceToken;

    @Builder
    public LogoutDTO(String accessToken, String refreshToken, String deviceToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.deviceToken = deviceToken;
    }
}
