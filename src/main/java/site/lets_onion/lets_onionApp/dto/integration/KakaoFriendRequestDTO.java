package site.lets_onion.lets_onionApp.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

@Data
public class KakaoFriendRequestDTO {
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("elements")
    private List<FriendFromKakao> friends;
}
