package site.lets_onion.lets_onionApp.repository.member;

import java.util.List;
import java.util.Optional;
import site.lets_onion.lets_onionApp.domain.member.Member;

public interface BaseMemberRepository {

  /*유저 ID로 단건 조회*/
  Member findByMemberId(Long memberId);

  /*로그인 시 카카오 ID로 조회*/
  Optional<Member> findByKakaoId(Long kakaoId);

  /*카카오 ID 배열로 모두 조회*/
  List<Member> findAllByKakaoId(Long memberId, List<Long> kakaoIds);

  /*자신과 친구가 아닌 유저들만 조회*/
  List<Member> findAllNotFriend(Long memberId, List<Long> memberIds);

  /*닉네임으로 조회*/
  Member findByNickname(String nickname);

  /*모든 디바이스 토큰을 페치 조인하여 조회*/
  Member findWithDeviceTokens(Long memberId);
}
