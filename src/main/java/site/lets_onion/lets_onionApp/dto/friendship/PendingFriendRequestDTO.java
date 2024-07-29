package site.lets_onion.lets_onionApp.dto.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;

@Data
public class PendingFriendRequestDTO {

  @JsonProperty("request_id")
  private Long id;
  private FriendDTO member;

  public PendingFriendRequestDTO(Friendship request) {
    this.id = request.getId();
    this.member = new FriendDTO(request.getFromMember());
  }
}
