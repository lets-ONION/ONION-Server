package site.lets_onion.lets_onionApp.repository.member;

import site.lets_onion.lets_onionApp.domain.member.Member;

import java.util.List;

public interface BaseMemberRepository {

    /*카카오 ID로 조회*/
    List<Member> findByKakaoId(Long kakaoId);


    /*닉네임으로 조회*/
    Member findByNickname(String nickname);


    /*모든 디바이스 토큰을 페치 조인하여 조회*/
    Member findWithDeviceTokens(Long memberId);
}
