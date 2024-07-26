package site.lets_onion.lets_onionApp.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.member.DeviceToken;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.jwt.LogoutDTO;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.*;
import site.lets_onion.lets_onionApp.dto.push.PushNotificationDTO;
import site.lets_onion.lets_onionApp.repository.deviceToken.DeviceTokenRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.push.PushType;
import site.lets_onion.lets_onionApp.util.redis.KakaoRedisConnector;
import site.lets_onion.lets_onionApp.util.redis.KakaoTokens;
import site.lets_onion.lets_onionApp.util.redis.ServiceRedisConnector;
import site.lets_onion.lets_onionApp.util.request.KakaoRequest;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.List;
import java.util.Optional;

@Service @Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final DeviceTokenRepository deviceTokenRepository;
    private final JwtProvider jwtProvider;
    private final ServiceRedisConnector serviceRedisConnector;
    private final KakaoRedisConnector kakaoRedisConnector;

    @Value("${kakao.apiKey}")
    private String clientId;
    private String kakaoCodeUri = "https://kauth.kakao.com/oauth/authorize?response_type=code";
    private final KakaoRequest kakaoRequest;

    @Override
    public String getRedirectUri(Redirection redirection) {
        return kakaoCodeUri + "&client_id=" + clientId +
                "&redirect_uri=" + redirection.getRedirectUri() +
                "&scope=friends,talk_message";
    }


    @Override
    @Transactional
    public ResponseDTO<LoginDTO> login(String code, Redirection redirection) {
        KakaoTokenResponseDTO tokenResponse = kakaoRequest.requestKakaoAuthToken(code, redirection);
        Long kakaoId = kakaoRequest.requestKakaoMemberInfo(tokenResponse.getAccessToken()).getKakaoId();

        Member member;
        Optional<Member> searched = memberRepository.findByKakaoId(kakaoId);
        boolean existMember;
        if (searched.orElse(null) == null) {
             member = createMember(kakaoId);
             existMember = false;
        } else {
            member = searched.get();
            existMember = true;
        }
        LoginDTO loginDTO = new LoginDTO(member,
                jwtProvider.createAccessToken(member.getId()),
                jwtProvider.createRefreshToken(member.getId()),
                existMember
        );
        kakaoRedisConnector.setWithTtl(
                member.getId(),
                new KakaoTokens(tokenResponse.getAccessToken(), tokenResponse.getRefreshToken()),
                (long) tokenResponse.getRefreshExpiresIn()
        );
        if (existMember) {
            return new ResponseDTO<>(loginDTO, Responses.OK);
        } else {
            return new ResponseDTO<>(loginDTO, Responses.CREATED);
        }
    }


    @Override
    @Transactional
    public ResponseDTO<Boolean> logout(Long memberId, LogoutDTO logoutDTO) {
        jwtProvider.logout(logoutDTO.getAccessToken(), logoutDTO.getRefreshToken());
        deviceTokenRepository.deleteDeviceToken(memberId, logoutDTO.getDeviceToken());
        Member member = findMember(memberId);
        try {
            kakaoRequest.requestKakaoLogout(member, kakaoRedisConnector.get(memberId).getAccessToken());
            return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new CustomException(Exceptions.KAKAO_LOGOUT_FAILED);
        }
    }


    @Override
    public ResponseDTO<TokenDTO> tokenReissue(String refreshToken) {
        TokenDTO tokens = jwtProvider.refreshAccessToken(refreshToken);
        return new ResponseDTO<>(tokens, Responses.OK);
    }


    @Override
    @Transactional
    public ResponseDTO<StatusMessageDTO> updateStatusMessage(Long memberId, String message) {
        serviceRedisConnector.setWithTtl(memberId.toString(), message, 86400000L); // 24시간 유지
        return new ResponseDTO<>(new StatusMessageDTO(message),
                Responses.CREATED);
    }


    @Override
    @Transactional
    public ResponseDTO<StatusMessageDTO> getStatusMessage(Long memberId) {
        String message = serviceRedisConnector.get(memberId.toString());
        return new ResponseDTO<>(new StatusMessageDTO(message),
                Responses.OK);
    }


    @Override
    @Transactional
    public ResponseDTO<MemberInfoDTO> updateNickname(Long memberId, String nickname) {
        Member member = findMember(memberId);
        member.updateNickname(nickname);
        return new ResponseDTO<>(new MemberInfoDTO(member),
                Responses.OK);
    }


    @Override
    public ResponseDTO<MemberInfoDTO> getMemberInfo(Long memberId) {
        Member member = findMember(memberId);
        return new ResponseDTO<>(new MemberInfoDTO(member),
                Responses.OK);
    }


    @Override
    @Transactional
    public ResponseDTO<Boolean> saveDeviceToken(Long memberId, String deviceToken) {
        Member member = memberRepository.findWithDeviceTokens(memberId);
        List<DeviceToken> deviceTokens = member.getDeviceTokens();
        for (DeviceToken dt : deviceTokens) {
            if (dt.getDeviceToken().equals(deviceToken)) {
                deviceTokenRepository.delete(dt);
                throw new CustomException(Exceptions.ALREADY_REGISTERED);
            }
        }
        DeviceToken deviceTokenEntity = new DeviceToken(member, deviceToken);
        deviceTokenRepository.save(deviceTokenEntity);
        return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
    }


    @Override
    @Transactional
    public ResponseDTO<PushNotificationDTO> modifyPushSetting(Long memberId, PushType pushType) {
        Member member = findMember(memberId);
        if (pushType.equals(PushType.TRADING)) {
            member.getPushNotification().changeTradeRequest();
        } else if (pushType.equals(PushType.FRIEND_REQUEST)) {
            member.getPushNotification().changeFriendRequest();
        } else if (pushType.equals(PushType.ALL)) {
            member.getPushNotification().changeEveryone();
        }
        return new ResponseDTO<>(new PushNotificationDTO(member)
                , Responses.OK);
    }

    @Override
    public ResponseDTO<KakaoScopesDTO> checkKakaoScopes(Long memberId) {
        Member member = findMember(memberId);
        String kakaoToken = kakaoRedisConnector.get(member.getId()).getAccessToken();
        KakaoScopesDTO response = kakaoRequest.requestKakaoScopes(member, kakaoToken);
        return new ResponseDTO<>(response, Responses.OK);
    }


    /*새로운 유저 데이터 생성*/
    private Member createMember(Long kakaoId) {
        Member member = Member.builder().kakaoId(kakaoId).build();
        return memberRepository.save(member);
    }


    /*유저 조회*/
    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new NullPointerException("User Not Exists"));
    }
}
