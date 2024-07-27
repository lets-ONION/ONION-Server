package site.lets_onion.lets_onionApp.dto.friendship;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.domain.member.Member;

@Data
public class FriendshipDTO {

  private Long id;
  @JsonProperty("from_member")
  private FriendMember fromMember;
  @JsonProperty("to_member")
  private FriendMember toMember;
  @JsonProperty("friendship_status")
  private FriendshipStatus status;
  @JsonProperty("created_at")
  private LocalDateTime createdAt;

  public FriendshipDTO(Friendship friendship) {
    this.id = friendship.getId();
    this.fromMember = new FriendMember(friendship.getFromMember());
    this.toMember = new FriendMember(friendship.getToMember());
    this.status = friendship.getStatus();
    this.createdAt = friendship.getCreatedAt();
  }

  @Data
  static public class FriendMember {

    private Long id;
    private String nickname;

    public FriendMember(Member member) {
      this.id = member.getId();
      this.nickname = member.getNickname();
    }
  }
}
