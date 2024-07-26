package site.lets_onion.lets_onionApp.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.dto.jwt.LogoutDTO;
import site.lets_onion.lets_onionApp.dto.jwt.RefreshTokenDTO;
import site.lets_onion.lets_onionApp.dto.jwt.TokenDTO;
import site.lets_onion.lets_onionApp.dto.member.*;
import site.lets_onion.lets_onionApp.dto.push.DeviceTokenRequestDTO;
import site.lets_onion.lets_onionApp.service.member.MemberService;
import site.lets_onion.lets_onionApp.service.member.Redirection;
import site.lets_onion.lets_onionApp.util.exception.ExceptionDTO;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;


    @GetMapping("/oauth/kakao/login")
    @Operation(summary = "백엔드 로그인", description = "백엔드 개발 시 로그인에 사용되는 API입니다.")
    @ApiResponse(responseCode = "301", description = "리다이렉트 성공")
    public ResponseEntity<?> getRedirect(HttpServletRequest request)
    {
        String addr = request.getRemoteAddr();
        Redirection redirection;
        if ("127.0.0.1".equals(addr) || "0:0:0:0:0:0:0:1".equals(addr)) {
            redirection = Redirection.LOCAL;
        } else {
            redirection = Redirection.SERVER;
        }
        String redirectUri = memberService.getRedirectUri(redirection);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUri));
        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
    }


    @GetMapping("/oauth/kakao/callback")
    @Operation(summary = "카카오 인증 API", description = "카카오 인가 코드를 받아 인증 처리를 하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "기존 사용자 로그인 성공")
    @ApiResponse(responseCode = "201", description = "신규 사용자 가입 및 로그인 성공",
    content = @Content(examples = @ExampleObject("{\"msg\": \"string\",\"code\": 0,\"data\":" +
            "{\"member\": {\"nickname\":\"string\",\"member_id\": 0},\"access_token\":" +
            "\"string\",\"refresh_token\": \"string\",\"exist_user\": false}}")))
    @ApiResponse(responseCode = "40x", description = "에러",
            content = @Content(schema = @Schema(implementation = ExceptionDTO.class)))
    public ResponseEntity<ResponseDTO<LoginDTO>> localLogin(
            HttpServletRequest request,
            @Parameter(description = "카카오 서버에서 발급 받은 인가코드입니다.")
            @RequestParam String code)
    {
        String addr = request.getRemoteAddr();
        Redirection redirection;
        if ("127.0.0.1".equals(addr) || "0:0:0:0:0:0:0:1".equals(addr)) {
            redirection = Redirection.LOCAL;
        } else {
            redirection = Redirection.SERVER;
        }
        ResponseDTO<LoginDTO> response = memberService.login(code, redirection);
        if (!response.getData().isExistUser()) {
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }


    @PostMapping("/token/refresh")
    @Operation(summary = "토큰 리프레시",
            description = "리프레시 토큰을 통해 새로운 액세스/리프레시 토큰을 발급받는 API입니다.")
    @ApiResponse(responseCode = "200", description = "토큰 리프레시 성공")
    @ApiResponse(responseCode = "40x", description = "에러", content = @Content(schema =
    @Schema(implementation = ExceptionDTO.class)))
    public ResponseEntity<ResponseDTO<TokenDTO>> tokenReissue(
            @RequestBody RefreshTokenDTO request)
    {
        return new ResponseEntity<>(
                memberService.tokenReissue(request.getRefreshToken()),
                HttpStatus.OK);
    }


    @PostMapping("/auth/logout")
    @Operation(summary = "로그아웃", description = "로그아웃을 처리하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "로그아웃 성공")
    public ResponseEntity<ResponseDTO<Boolean>> logout(HttpServletRequest request
    , @RequestBody LogoutDTO logoutDTO)
    {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.logout(memberId, logoutDTO), HttpStatus.OK);
    }


    @PutMapping("/nickname/update")
    @Operation(summary = "닉네임 수정", description = "닉네임을 수정하는 API입니다.")
    @ApiResponse(responseCode = "200", description = "닉네임 수정 성공")
    public ResponseEntity<ResponseDTO<MemberInfoDTO>> updateNickname(
            HttpServletRequest request, @RequestBody NicknameDTO dto)
    {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.updateNickname(memberId, dto.getNickname()),
                HttpStatus.OK);
    }


    @PostMapping("/status-message/update")
    @Operation(summary = "상태메시지 수정",
            description = "상태메시지를 작성하는 API입니다.24시간 후 삭제됩니다.")
    @ApiResponse(responseCode = "200", description = "상태메시지 작성 성공")
    public ResponseEntity<ResponseDTO<StatusMessageDTO>> updateStatusMessage(
            HttpServletRequest request, @RequestBody StatusMessageDTO dto
    ) {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(memberService
                .updateStatusMessage(memberId,dto.getStatusMessage()),
                HttpStatus.OK);
    }


    @GetMapping("/status-message/get")
    @Operation(summary = "상태메시지 조회",
    description = "상태메시지를 조회하는 API입니다. 쿼리파라미터가 없으면 자신의 상태메시지를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "상태메시지 조회 성공")
    public ResponseEntity<ResponseDTO<StatusMessageDTO>> getStatusMessage(
            HttpServletRequest request,
            @Nullable @RequestParam(name = "member_id") Long memberId
    ) {
        Long id;
        if (memberId == null) {
            id = jwtProvider.getMemberId(request);
        } else {
            id = memberId;
        }
        return new ResponseEntity<>(memberService.getStatusMessage(id),
                HttpStatus.OK);
    }


    @GetMapping("/info/get")
    @Operation(summary = "유저 정보 조회",
    description = "유저 정보를 조회하는 API입니다. 쿼리파라미터가 없으면 자신의 정보를 조회합니다.")
    @ApiResponse(responseCode = "200", description = "유저 정보 조회 성공")
    public ResponseEntity<ResponseDTO<MemberInfoDTO>> getMemberInfo(
            HttpServletRequest request,
            @Nullable @RequestParam(name = "member_id") Long memberId
    ) {
        Long id;
        if (memberId == null) {
            id = jwtProvider.getMemberId(request);
        } else {
            id = memberId;
        }
        return new ResponseEntity<>(memberService.getMemberInfo(id),
                HttpStatus.OK);
    }


    @PostMapping("/device-token/save")
    @Operation(summary = "디바이스 토큰 업데이트",
            description = "디바이스 토큰을 서버에 전송하는 API입니다. 기존 토큰과 같을 시 200이 응답됩니다.")
    @ApiResponse(responseCode = "200", description = "디바이스 토큰 업데이트 성공")
    public ResponseEntity<ResponseDTO<Boolean>> updateDeviceToken(
            HttpServletRequest request,
            @RequestBody DeviceTokenRequestDTO dto)
    {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.saveDeviceToken(
                        memberId, dto.getDeviceToken()
                ),HttpStatus.OK);
    }

    @GetMapping("/kakao/scope")
    @Operation(summary = "카카오 동의항목 확인",
    description = "유저의 카카오 정보 동의 내역을 확인하는 API입니다.")
    @ApiResponse(responseCode = "200", description = """
    자세한 내용은
    https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#check-consent-response-body-scope
    """)
    public ResponseEntity<ResponseDTO<KakaoScopesDTO>> kakaoTest(
            HttpServletRequest request
    ) {
        Long memberId = jwtProvider.getMemberId(request);
        return new ResponseEntity<>(
                memberService.checkKakaoScopes(memberId),
                HttpStatus.OK);
    }
}
