package site.lets_onion.lets_onionApp.repository.friendship;

import java.util.List;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;

public interface BaseFriendshipRepository {

  /*친구 요청 단건 조회*/
  Friendship findByFriendshipId(Long friendshipId);

  /*친구 목록 조회*/
  List<Friendship> findFriendsByMemberId(Long memberId);

  /*보낸 친구 신청 목록 조회*/
  List<Friendship> findSentFriendRequestsByMemberId(Long memberId);

  /*받은 친구 신청 목록 조회*/
  List<Friendship> findReceivedFriendRequestsByMemberId(Long memberId);

  /*두 유저 간의 Friendship 엔티티 조회*/
  Friendship findByMemberIdAndFriendId(Long memberId, Long friendId);
}
