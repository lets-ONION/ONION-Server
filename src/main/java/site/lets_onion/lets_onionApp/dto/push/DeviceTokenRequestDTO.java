package site.lets_onion.lets_onionApp.dto.push;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 클라이언트에서 유저의 디바이스 토큰을
 * 서버로 전달하는 DTO
 */
@Data
public class DeviceTokenRequestDTO {

    @JsonProperty("device_token")
    private String deviceToken;

    @JsonCreator
    public DeviceTokenRequestDTO(String deviceToken) {
        this.deviceToken = deviceToken;
    }
}
