package site.lets_onion.lets_onionApp.service.member;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
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
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.List;
import java.util.Map;
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
    @Value("${kakao.adminKey}")
    private String adminKey;
    private String kakaoCodeUri = "https://kauth.kakao.com/oauth/authorize?response_type=code";
    private WebClient tokenWebClient = WebClient.create("https://kauth.kakao.com/oauth/token");
    private WebClient memberInfoWebClient = WebClient.create("https://kapi.kakao.com/v2/user/me");
    private WebClient logoutWebClient = WebClient.create("https://kapi.kakao.com/v1/user/logout");

    @Override
    public String getRedirectUri(Redirection redirection) {
        return kakaoCodeUri + "&client_id=" + clientId +
                "&redirect_uri=" + redirection.getRedirectUri();
    }

    @Override
    @Transactional
    public ResponseDTO<LoginDTO> login(String code, Redirection redirection) {
        KakaoTokenResponseDTO tokenResponse = requestKakaoToken(code, redirection);
        Long kakaoId = requestMemberInfo(tokenResponse.getAccessToken());

        List<Member> searchedResult = memberRepository.findByKakaoId(kakaoId);
        Member member;
        boolean existMember;
        if (searchedResult.isEmpty()) {
             member = createMember(kakaoId);
             existMember = false;
        } else {
            member = searchedResult.get(0);
            existMember = true;
        }
        LoginDTO loginDTO = new LoginDTO(member,
                jwtProvider.createAccessToken(member.getId()),
                jwtProvider.createRefreshToken(member.getId()),
                existMember
        );
        for (int i = 0; i < 100; i++) {
            System.out.println("tokenResponse = " + tokenResponse);
        }
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
        if(!requestKakaoLogout(memberId)) {
            throw new CustomException(Exceptions.KAKAO_LOGOUT_FAILED);
        }
        return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
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


    /*카카오 인증 서버에 액세스 토큰 요청*/
    private KakaoTokenResponseDTO requestKakaoToken(String code, Redirection redirection) {
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("grant_type", "authorization_code");
        formData.add("client_id", clientId);
        formData.add("redirect_uri", redirection.getRedirectUri());
        formData.add("code", code);

        KakaoTokenResponseDTO response = tokenWebClient.post()
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(formData)
                .retrieve().bodyToMono(KakaoTokenResponseDTO.class)
                .block();
        return Optional.ofNullable(response)
                .orElseThrow(() -> new CustomException(Exceptions.KAKAO_AUTH_FAILED_WITH_TOKEN));
    }


    /*카카오 인증 서버에 유저 정보 요청*/
    private Long requestMemberInfo(String token) {
        KakaoMemberInfoDTO response = memberInfoWebClient.get()
                .header("Authorization", "Bearer " + token)
                .retrieve().bodyToMono(KakaoMemberInfoDTO.class)
                .block();
        return response.getKakaoId();
    }


    /*새로운 유저 데이터 생성*/
    private Member createMember(Long kakaoId) {
        Member member = Member.builder().kakaoId(kakaoId).build();
        return memberRepository.save(member);
    }


    /*카카오 인증 서버에 로그아웃 요청*/
    private boolean requestKakaoLogout(Long memberId) {
        Member member = findMember(memberId);
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.add("target_id_type", "user_id");
        formData.add("target_id", member.getKakaoId().toString());

        Map<String, Long> response = logoutWebClient.post().contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .header("Authorization", "KakaoAK " + adminKey)
                .bodyValue(formData).retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Long>>() {})
                .block();
        if (!response.containsKey("id")) {
            return false;
        }
        kakaoRedisConnector.remove(memberId);
        return true;
    }


    /*유저 조회*/
    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new NullPointerException("User Not Exists"));
    }
}
