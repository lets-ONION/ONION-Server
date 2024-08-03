package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.dto.member.MypageMemberInfoRequestDTO;
import site.lets_onion.lets_onionApp.dto.member.MypageMemberInfoResponseDTO;
import site.lets_onion.lets_onionApp.dto.onion.GainedOnionDTO;
import site.lets_onion.lets_onionApp.service.member.MemberService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MypageController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    @Operation(summary = "유저 정보 리턴", description = """
      유저의 닉네임, 프로필 이미지 정보를 보여주는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<MypageMemberInfoResponseDTO> getMypageMemberInfo(
            HttpServletRequest request
    ){
        Long memberId = jwtProvider.getMemberId(request);
        return memberService.getMypageMemberInfo(memberId);
    }

    @PutMapping("")
    @Operation(summary = "유저 정보 수정", description = """
      유저의 닉네임, 프로필 이미지 정보를 수정하는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<MypageMemberInfoResponseDTO> updateMypageMemberInfo(
            HttpServletRequest request,
            @RequestBody MypageMemberInfoRequestDTO dto
            ){
        Long memberId = jwtProvider.getMemberId(request);
        return memberService.updateMypageMemberInfo(memberId, dto);
    }


    @GetMapping("/onions")
    @Operation(summary = "획득한 양파 이미지 리턴", description = """
      획득한 양파 이미지와 긍정/부정 0단계 양파 이미지를 리턴하는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<GainedOnionDTO> getGainedOnions(
            HttpServletRequest request
    ){
        Long memberId = jwtProvider.getMemberId(request);
        return memberService.getGainedOnions(memberId);
    }

}
