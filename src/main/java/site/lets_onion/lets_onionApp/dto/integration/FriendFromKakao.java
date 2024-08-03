package site.lets_onion.lets_onionApp.dto.integration;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FriendFromKakao {

  private Long id;
  private String uuid;
  @JsonProperty("profile_nickname")
  private String profileNickname;
  @JsonProperty("profile_thumbnail_image")
  private String profileImage;
}
