package site.lets_onion.lets_onionApp.dto.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class FriendDTO {

  @JsonProperty("member_id")
  private Long id;
  private String nickname;

  public FriendDTO(Member friend) {
    this.id = friend.getId();
    this.nickname = friend.getNickname();
  }
}
