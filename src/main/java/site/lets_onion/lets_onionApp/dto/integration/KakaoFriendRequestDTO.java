package site.lets_onion.lets_onionApp.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class KakaoFriendRequestDTO {
    @JsonProperty("total_count")
    private int totalCount;
    @JsonProperty("Friend")
    private List<Friend> friends;


    @Data
    static public class Friend {

        private Long id;
        private String uuid;
        @JsonProperty("profile_nickname")
        private String profileNickname;
        @JsonProperty("profile_thumbnail_image")
        private String profileImage;
    }
}
