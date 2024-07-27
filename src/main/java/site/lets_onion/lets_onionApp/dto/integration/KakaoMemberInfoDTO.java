package site.lets_onion.lets_onionApp.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoMemberInfoDTO {

    @JsonProperty("id")
    private Long kakaoId;

}
