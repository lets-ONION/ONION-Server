package site.lets_onion.lets_onionApp.service.member;

import site.lets_onion.lets_onionApp.dto.integration.FriendRequestDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoScopesDTO;
import site.lets_onion.lets_onionApp.dto.jwt.LogoutDTO;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.*;
import site.lets_onion.lets_onionApp.dto.push.PushNotificationDTO;
import site.lets_onion.lets_onionApp.util.push.PushType;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface MemberService {

    /*리다이렉트 URI 반환*/
    String getRedirectUri(Redirection redirection);

    /*유저 로그인*/
    ResponseDTO<LoginDTO> login(String code, Redirection redirection);

    /*로그아웃*/
    ResponseDTO<Boolean> logout(Long memberId, LogoutDTO logoutDTO);

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

    /*유저 디바이스 토큰 추가*/
    ResponseDTO<Boolean> saveDeviceToken(Long memberId, String deviceToken);

    /*알림 설정 업데이트*/
    ResponseDTO<PushNotificationDTO> modifyPushSetting(Long memberId, PushType pushType);

    /*유저 동의 항목 조회*/
    ResponseDTO<KakaoScopesDTO> checkKakaoScopes(Long memberId);

    /*유저의 카카오 친구 목록 조회*/
    ResponseDTO<FriendRequestDTO> requestKakaoFriends(Long memberId, int offset);
}
