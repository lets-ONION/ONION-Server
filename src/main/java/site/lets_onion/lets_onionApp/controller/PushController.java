package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.lets_onion.lets_onionApp.dto.push.PushNotificationDTO;
import site.lets_onion.lets_onionApp.dto.push.PushTestRequestDTO;
import site.lets_onion.lets_onionApp.dto.push.PushTestResponseDTO;
import site.lets_onion.lets_onionApp.service.member.MemberService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.push.PushProvider;
import site.lets_onion.lets_onionApp.util.push.PushType;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/push")
public class PushController {

  private final MemberService memberService;
  private final JwtProvider jwtProvider;
  private final PushProvider pushProvider;

  @GetMapping("/notification/config/get")
  @Operation(summary = "푸시 알림 설정 조회", description = "푸시 알림 설정을 조회하는 API입니다.")
  @ApiResponse(responseCode = "200", description = "조회 성공")
  public ResponseDTO<PushNotificationDTO> getPushConfiguration(HttpServletRequest request) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.getPushConfiguration(memberId);
  }

  @PatchMapping("/notification/config/update")
  @Operation(summary = "푸시 알림 설정", description =
      """
          푸시 알림을 설정하는 API입니다.<br>
          pushType은 다음과 같습니다.<br>
          **FRIEND_REQUEST**: 친구 요청<br>
          **FRIEND_RESPONSE**: 상대방의 친구 수락<br>
          **TRADE_REQUEST**: 교환 신청<br>
          **TRADE_RESPONSE**: 상대방의 교환 수락<br>
          **WATERING_TIME**: 양파에 물 주는 시간
          """)
  @ApiResponse(responseCode = "200", description = "수정 성공")
  public ResponseDTO<PushNotificationDTO> modifyPushConfiguration(
      HttpServletRequest request, @RequestParam PushType type) {
    Long memberId = jwtProvider.getMemberId(request);
    return memberService.modifyPushSetting(memberId, type);
  }


  @PostMapping("/test")
  @Operation(summary = "푸시 메시지를 테스트하는 API입니다.",
      description = "백엔드쪽에서 테스트할 수 있는 방법이 없어서 우선 " +
          "받는 요청은 그대로 다 돌려보내도록 했습니다ㅜ")
  public ResponseDTO<PushTestResponseDTO> pushTest(
      HttpServletRequest request,
      @RequestBody PushTestRequestDTO pushTestRequestDTO) {
    Long memberId = jwtProvider.getMemberId(request);
    return pushProvider.pushTest(memberId, pushTestRequestDTO);
  }
}
