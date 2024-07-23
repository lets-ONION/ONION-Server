package site.lets_onion.lets_onionApp.repository.member;

import site.lets_onion.lets_onionApp.domain.Member;

public interface BaseMemberRepository {

    /*카카오 ID로 조회*/
    Member findByKakaoId(String kakaoId);


    /*닉네임으로 조회*/
    Member findByNickname(String nickname);


    /*닉네임 수정*/
    Member updateNickname(Long memberId, String nickname);
}
