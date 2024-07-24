package site.lets_onion.lets_onionApp.service.member;

import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.LoginDTO;
import site.lets_onion.lets_onionApp.dto.member.MemberInfoDTO;
import site.lets_onion.lets_onionApp.dto.member.StatusMessageDTO;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface MemberService {

    /*리다이렉트 URI 반환*/
    String getRedirectUri(Redirection redirection);

    /*유저 로그인*/
    ResponseDTO<LoginDTO> login(String code, Redirection redirection);

    /*로그아웃*/
    ResponseDTO<?> logout(Long memberId, String accessToken, String refreshToken);

    /*토큰 리프레시*/
    ResponseDTO<TokenDTO> tokenReissue(String refreshToken);

    /*상태메시지 작성*/
    ResponseDTO<StatusMessageDTO> updateStatusMessage(Long memberId, String message);

    /*상태메시지 조회*/
    ResponseDTO<StatusMessageDTO> getStatusMessage(Long memberId);

    /*닉네임 수정*/
    ResponseDTO<MemberInfoDTO> updateNickname(Long memberId, String nickname);

    /*유저 정보 조회*/
    ResponseDTO<MemberInfoDTO> getMemberInfo(Long memberId);
}
