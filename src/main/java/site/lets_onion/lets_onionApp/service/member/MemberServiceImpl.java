package site.lets_onion.lets_onionApp.service.member;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.DeviceToken;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.domain.onion.OnionLevel;
import site.lets_onion.lets_onionApp.domain.onionBook.CollectedOnion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;
import site.lets_onion.lets_onionApp.dto.integration.KakaoFriendRequestDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoScopesDTO;
import site.lets_onion.lets_onionApp.dto.integration.KakaoTokenResponseDTO;
import site.lets_onion.lets_onionApp.dto.jwt.LogoutDTO;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.AppLoginDTO;
import site.lets_onion.lets_onionApp.dto.member.LoginDTO;
import site.lets_onion.lets_onionApp.dto.member.MemberInfoDTO;
import site.lets_onion.lets_onionApp.dto.member.StatusMessageDTO;
import site.lets_onion.lets_onionApp.dto.member.*;
import site.lets_onion.lets_onionApp.dto.onion.GainedOnionDTO;
import site.lets_onion.lets_onionApp.dto.onion.OnionImageUrlDTO;
import site.lets_onion.lets_onionApp.dto.onionBook.OnionDTO;
import site.lets_onion.lets_onionApp.dto.push.PushNotificationDTO;
import site.lets_onion.lets_onionApp.repository.deviceToken.DeviceTokenRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.jwt.TokenType;
import site.lets_onion.lets_onionApp.util.push.PushType;
import site.lets_onion.lets_onionApp.util.redis.KakaoRedisConnector;
import site.lets_onion.lets_onionApp.util.redis.KakaoTokens;
import site.lets_onion.lets_onionApp.util.redis.ServiceRedisConnector;
import site.lets_onion.lets_onionApp.util.request.KakaoRequest;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private final MemberRepository memberRepository;
  private final DeviceTokenRepository deviceTokenRepository;
  private final OnionBookRepository onionBookRepository;
  private final JwtProvider jwtProvider;
  private final ServiceRedisConnector serviceRedisConnector;
  private final KakaoRedisConnector kakaoRedisConnector;

  @Value("${kakao.apiKey}")
  private String clientId;
  private final KakaoRequest kakaoRequest;


  /**
   * 유저의 접속 위치에 따라 적절한 리다이렉트 uri를 반환합니다.
   *
   * @param redirection
   * @return
   */
  @Override
  public String getRedirectUri(Redirection redirection) {
    return "https://kauth.kakao.com/oauth/authorize"
        + "?response_type=code&client_id=" + clientId +
        "&redirect_uri=" + redirection.getRedirectUri() +
        "&scope=friends,talk_message";
  }


  @Override
  @Transactional
  public ResponseDTO<LoginDTO> login(String code, Redirection redirection) {
    KakaoTokenResponseDTO tokenResponse = kakaoRequest
        .requestKakaoAuthToken(code, redirection);

    Long kakaoId = kakaoRequest.requestKakaoMemberInfo(
        tokenResponse.getAccessToken()
    ).getKakaoId();

    Optional<Member> searchedMember = memberRepository.findByKakaoId(kakaoId);
    boolean existMember;
    if (searchedMember.isEmpty()) {
      searchedMember = Optional.of(createMember(kakaoId));
      existMember = false;
    } else {
      existMember = true;
    }
    Member member = searchedMember.get();
    LoginDTO loginDTO = new LoginDTO(member,
        jwtProvider.createToken(member.getId(), TokenType.ACCESS),
        jwtProvider.createToken(member.getId(), TokenType.REFRESH),
        existMember
    );
    kakaoRedisConnector.setWithTtl(
        member.getId(), new KakaoTokens
            (
                tokenResponse.getAccessToken(), tokenResponse.getRefreshToken()
            ),
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
  public ResponseDTO<LoginDTO> loginInApp(AppLoginDTO request) {
    return loginWithKakao(request);
  }


  /**
   * 서비스 내에서 발급한 JWT 토큰과 카카오 인증 서버에서 발급 받은<br> JWT 토큰을 모두 폐기하여 로그아웃합니다.<br> 이 때, 해당 기기의 디바이스 토큰도 함께
   * 삭제합니다.
   *
   * @param memberId
   * @param logoutDTO
   * @return
   */
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


  /**
   * 리프레시 토큰을 사용하여 새로운 액세스/리프레시 토큰을 발급합니다.<br> 기존 리프레시 토큰은 블랙리스트에 올립니다.
   *
   * @param refreshToken
   * @return
   */
  @Override
  public ResponseDTO<TokenDTO> tokenReissue(String refreshToken) {
    TokenDTO tokens = jwtProvider.refreshAccessToken(refreshToken);
    return new ResponseDTO<>(tokens, Responses.OK);
  }


  /**
   * 상태메시지를 업데이트합니다.
   *
   * @param memberId
   * @param message
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<StatusMessageDTO> updateStatusMessage(Long memberId, String message) {
    serviceRedisConnector.setWithTtl(memberId.toString(), message, 86400000L); // 24시간 유지
    return new ResponseDTO<>(new StatusMessageDTO(message),
        Responses.CREATED);
  }


  /**
   * 상태메시지를 조회합니다.
   *
   * @param memberId
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<StatusMessageDTO> getStatusMessage(Long memberId) {
    String message = serviceRedisConnector.get(memberId.toString());
    return new ResponseDTO<>(new StatusMessageDTO(message),
        Responses.OK);
  }


  /**
   * 닉네임을 업데이트합니다.
   *
   * @param memberId
   * @param nickname
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<MemberInfoDTO> updateNickname(Long memberId, String nickname) {
    Member member = findMember(memberId);
    member.updateNickname(nickname);
    return new ResponseDTO<>(new MemberInfoDTO(member),
        Responses.OK);
  }


  /**
   * 유저 정보를 조회합니다.
   *
   * @param memberId
   * @return
   */
  @Override
  public ResponseDTO<MemberInfoDTO> getMemberInfo(Long memberId) {
    Member member = findMember(memberId);
    return new ResponseDTO<>(new MemberInfoDTO(member),
        Responses.OK);
  }


  /**
   * 새로운 디바이스 토큰을 저장합니다.
   *
   * @param memberId
   * @param deviceToken
   * @return
   */
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

    DeviceToken deviceTokenEntity =
        DeviceToken.builder()
            .member(member)
            .deviceToken(deviceToken)
            .build();
    deviceTokenRepository.save(deviceTokenEntity);
    return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
  }


  /**
   * 푸시 알림 설정을 수정합니다.
   *
   * @param memberId
   * @param pushType
   * @return
   */
  @Override
  @Transactional
  public ResponseDTO<PushNotificationDTO> modifyPushSetting(Long memberId, PushType pushType) {
    Member member = findMember(memberId);
    member.getPushNotification().updateNotification(pushType);
    return new ResponseDTO<>(new PushNotificationDTO(member),
        Responses.OK);
  }


  /**
   * 유저의 카카오 계정 동의 정보를 조회합니다.
   *
   * @param memberId
   * @return
   */
  @Override
  public ResponseDTO<KakaoScopesDTO> checkKakaoScopes(Long memberId) {
    Member member = findMember(memberId);
    String kakaoToken = kakaoRedisConnector.get(member.getId()).getAccessToken();
    KakaoScopesDTO response = kakaoRequest.requestKakaoScopes(member, kakaoToken);
    return new ResponseDTO<>(response, Responses.OK);
  }


  /**
   * 카카오톡 친구 목록을 조회합니다.
   *
   * @param memberId
   * @return
   */
  @Override
  public ResponseDTO<KakaoFriendRequestDTO> requestKakaoFriends(Long memberId, int offset) {
    Member member = findMember(memberId);
    String kakaoToken = kakaoRedisConnector.get(memberId).getAccessToken();
    return new ResponseDTO<>(
        kakaoRequest.kakaoRequestFriends(member, kakaoToken, offset),
        Responses.OK
    );
  }


  /**
   * 새로운 유저 데이터를 생성합니다.
   *
   * @param kakaoId
   * @return
   */
  /*새로운 유저 데이터 생성*/
  private Member createMember(Long kakaoId) {
    Member member = Member.builder().kakaoId(kakaoId).build();
    return memberRepository.save(member);
  }


  /**
   * 예외를 처리하며 유저를 조회합니다.
   *
   * @param memberId
   * @return
   */
  /*유저 조회*/
  private Member findMember(Long memberId) {
    return memberRepository.findById(memberId).orElseThrow(() ->
        new CustomException(Exceptions.MEMBER_NOT_EXIST));
  }


  /*카카오 로그인*/
  private ResponseDTO<LoginDTO> loginWithKakao(AppLoginDTO request) {
      Long kakaoId = kakaoRequest.requestKakaoMemberInfo(
              request.getAccessToken()
      ).getKakaoId();

      Optional<Member> searchedMember = memberRepository.findByKakaoId(kakaoId);
      boolean existMember;
      if (searchedMember.isEmpty()) {
          searchedMember = Optional.of(createMember(kakaoId));
          existMember = false;
      } else {
          existMember = true;
      }
      Member member = searchedMember.get();
      LoginDTO loginDTO = new LoginDTO(member,
              jwtProvider.createToken(member.getId(), TokenType.ACCESS),
              jwtProvider.createToken(member.getId(), TokenType.REFRESH),
              existMember
      );
      kakaoRedisConnector.setWithTtl(
              member.getId(), new KakaoTokens
                      (
                              request.getAccessToken(), request.getRefreshToken()
                      ),
              5183999L
      );
      if (existMember) {
          return new ResponseDTO<>(loginDTO, Responses.OK);
      } else {
          return new ResponseDTO<>(loginDTO, Responses.CREATED);
      }
  }


    /**
     * 마이페이지에서 유저 정보를 조회합니다.
     * @param memberId
     * @return
     */
    @Override
    public ResponseDTO<MypageMemberInfoResponseDTO> getMypageMemberInfo(Long memberId) {
        Member member = findMember(memberId);
        MypageMemberInfoResponseDTO dto = new MypageMemberInfoResponseDTO(member);
        return new ResponseDTO<>(dto, Responses.OK);
    }

    /**
     * 카카오톡 친구 목록을 조회합니다.
     * @param memberId
     * @param dto
     * @return
     */
    @Override
    @Transactional
    public ResponseDTO<MypageMemberInfoResponseDTO> updateMypageMemberInfo(Long memberId, MypageMemberInfoRequestDTO dto) {
        Member member = findMember(memberId);
        member.updateNickname(dto.getNickname());
        member.updateUserImageUrl(dto.getUserImageUrl());
        MypageMemberInfoResponseDTO mypageMemberInfoResponseDTO = new MypageMemberInfoResponseDTO(member);
        return new ResponseDTO<>(mypageMemberInfoResponseDTO, Responses.OK);
    }

  @Override
  public ResponseDTO<GainedOnionDTO> getGainedOnions(Long memberId) {
    OnionBook onionBook = onionBookRepository.findByMemberId(memberId);
    // 1개 이상 모인 양파 리스트
    List<OnionImageUrlDTO> onionImageUrlDTOS = new ArrayList<>();
    // 아직 도감에서 모은 양파가 없을 경우를 대비해 디폴트 프로필 이미지로 긍정/부정양파 0단계 제공
    onionImageUrlDTOS.add(new OnionImageUrlDTO(OnionLevel.ZERO.getPosImageUrl()));
    onionImageUrlDTOS.add(new OnionImageUrlDTO(OnionLevel.ZERO.getNegImageUrl()));
    for (OnionType onionType: OnionType.values()) {
      CollectedOnion collectedOnion = onionBook.getOnion(onionType);
      if (collectedOnion.getCollectedQuantity() == 0) {
        continue;
      }
      onionImageUrlDTOS.add(new OnionImageUrlDTO(onionType.getImageUrl()));
    }
    GainedOnionDTO dto = new GainedOnionDTO(onionImageUrlDTOS);
    return new ResponseDTO<>(dto, Responses.OK);
  }
}
