package site.lets_onion.lets_onionApp.service.friendship;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.friendship.Friendship;
import site.lets_onion.lets_onionApp.domain.friendship.FriendshipStatus;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.friendship.FriendshipDTO;
import site.lets_onion.lets_onionApp.repository.friendship.FriendshipRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.push.PushProvider;
import site.lets_onion.lets_onionApp.util.push.PushType;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FriendshipServiceImpl implements FriendshipService {

  private final FriendshipRepository friendshipRepository;
  private final MemberRepository memberRepository;
  private final PushProvider pushProvider;

  /**
   * 친구 요청을 보내는 메소드입니다.
   *
   * @param fromMemberId
   * @param toMemberId
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<FriendshipDTO> sendFriendRequest(Long fromMemberId, Long toMemberId) {
    Member fromMember = memberRepository.findByMemberId(fromMemberId);
    Member toMember = memberRepository.findByMemberId(toMemberId);
    Friendship friendRequest = Friendship.builder()
        .fromMember(fromMember)
        .toMember(toMember)
        .build();

    friendshipRepository.save(friendRequest);
    pushProvider.sendPushToOne(fromMember, toMember, PushType.FRIEND_REQUEST);
    return new ResponseDTO<>(
        new FriendshipDTO(friendRequest),
        Responses.OK);
  }

  /**
   * 친구 요청에 응답하는 메소드입니다.<br> 요청을 받은 사람이 아닐 시 예외를 발생시킵니다.
   *
   * @param memberId
   * @param friendshipId
   * @param toStatus
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<Boolean> updateFriendStatus(Long memberId, Long friendshipId,
      FriendshipStatus toStatus) {
    Friendship friendRequest = friendshipRepository.findByFriendshipId(friendshipId);
    if (!friendRequest.getToMember().getId().equals(memberId)) {
      throw new CustomException(Exceptions.ONLY_REQUESTED_MEMBER_CAN_RESPONSE);
    }

    friendRequest.updateStatus(toStatus);
    if (toStatus.equals(FriendshipStatus.ACCEPTED)) {
      pushProvider.sendPushToOne(
          friendRequest.getToMember(),
          friendRequest.getFromMember(),
          PushType.FRIEND_RESPONSE
      );
    }
    return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
  }

  /**
   * 친구를 목록에서 삭제합니다.<br> 이미 친구가 아닐 경우 예외를 발생시킵니다.
   *
   * @param memberId
   * @param friendId
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<Boolean> deleteFriend(Long memberId, Long friendId) {
    Friendship friendRequest = friendshipRepository
        .findByMemberIdAndFriendId(memberId, friendId);

    if (!friendRequest.getStatus().equals(FriendshipStatus.ACCEPTED)) {
      throw new CustomException(Exceptions.NOT_FRIENDS_ALREADY);
    }

    friendshipRepository.delete(friendRequest);
    return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
  }

  /**
   * 자신의 친구 목록을 조회합니다.
   * @param memberId
   * @return
   */
  @Override
  public ResponseDTO<List<FriendshipDTO>> getFriendList(Long memberId) {
    List<FriendshipDTO> friendList = friendshipRepository.findFriendsByMemberId(memberId)
        .stream().map(FriendshipDTO::new)
        .toList();

    return new ResponseDTO<>(friendList, Responses.OK);
  }

  /**
   * 받은 친구 요청 목록을 조회합니다.
   * @param memberId
   * @return
   */
  @Override
  public ResponseDTO<List<FriendshipDTO>> getReceivedFriendRequestList(Long memberId) {
    List<FriendshipDTO> receivedRequestList =
        friendshipRepository.findReceivedFriendRequestsByMemberId(memberId)
            .stream().map(FriendshipDTO::new)
            .toList();

    return new ResponseDTO<>(receivedRequestList, Responses.OK);
  }
}
