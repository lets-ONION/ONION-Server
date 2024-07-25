package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping("/notification/config")
    @Operation(summary = "푸시 알림 설정", description =
            """
            푸시 알림을 설정하는 API입니다.<br>
            pushType은 다음과 같습니다.<br>
            **TRADING**: 교환<br>
            **FRIEND_REQUEST**: 친구 요청<br>
            **ALL**: 전체 유저 푸시
            """)
    @ApiResponse(responseCode = "200", description = "수정 성공")
    public ResponseEntity<ResponseDTO<PushNotificationDTO>> modifyPushConfiguration(
            HttpServletRequest request, @RequestParam PushType type)
    {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.modifyPushSetting(memberId, type),
                HttpStatus.OK);
    }


    @PostMapping("/test")
    @Operation(summary = "푸시 메시지를 테스트하는 API입니다.")
    public ResponseEntity<ResponseDTO<PushTestResponseDTO>> pushTest(
            HttpServletRequest request,
            @RequestBody PushTestRequestDTO pushTestRequestDTO)
    {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                pushProvider.pushTest(memberId, pushTestRequestDTO), HttpStatus.OK);
    }
}
