package site.lets_onion.lets_onionApp.service.friendship;

import java.util.List;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.dto.friendship.FriendDTO;
import site.lets_onion.lets_onionApp.dto.friendship.FriendshipDTO;
import site.lets_onion.lets_onionApp.dto.friendship.PendingFriendRequestDTO;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface FriendshipService {

  /*친구 요청 보내기*/
  ResponseDTO<FriendshipDTO> sendFriendRequest(Long fromMemberId, Long toMemberId);

  /*친구 요청에 응답*/
  ResponseDTO<Boolean> updateFriendStatus(Long memberId, Long friendshipId, FriendshipStatus toStatus);

  /*친구 삭제*/
  ResponseDTO<Boolean> deleteFriend(Long memberId, Long friendId);

  /*친구 목록 조회*/
  ResponseDTO<List<FriendDTO>> getFriendList(Long memberId);

  /*받은 친구 신청 목록 조회*/
  ResponseDTO<List<PendingFriendRequestDTO>> getReceivedFriendRequestList(Long memberId);
}
