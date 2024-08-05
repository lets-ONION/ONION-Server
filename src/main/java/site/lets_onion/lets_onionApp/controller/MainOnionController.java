package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.dto.onion.*;
import site.lets_onion.lets_onionApp.service.onion.OnionService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main")
public class MainOnionController {

    private final OnionService onionService;
    private final JwtProvider jwtProvider;

    @GetMapping("")
    @Operation(summary = "메인페이지", description = """
      메인페이지를 조회하는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<OnionsDTO> getMainPage(
            HttpServletRequest request
    ) {
        Long memberId = jwtProvider.getMemberId(request);
        return onionService.getMainPage(memberId);
    }

    @PatchMapping("/naming")
    @Operation(summary = "양파 이름 지정", description = """
      양파 이름들을 지정하는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<OnionsDTO> namingOnions(
            HttpServletRequest request,
            @RequestBody NamingOnionsDTO namingOnionsDTO
    ) {
        Long memberId = jwtProvider.getMemberId(request);
        return onionService.saveOnionName(memberId, namingOnionsDTO);
    }

    @PostMapping("/positive/note")
    @Operation(summary = "긍정일기 작성", description = """
      긍정일기 작성/저장하는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<Boolean> postPosNote(
            HttpServletRequest request,
            @RequestBody PosNoteDTO posNoteDTO
            ){
        Long memberId = jwtProvider.getMemberId(request);
        return onionService.savePosNote(memberId, posNoteDTO);
    }

    @PatchMapping("/positive/water")
    @Operation(summary = "긍정양파 물 주기", description = """
      긍정양파를 한 단계 성장시키는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<PosOnionDTO> waterPosOnion(
            HttpServletRequest request
    ){
        Long memberId = jwtProvider.getMemberId(request);
        return onionService.waterPosOnion(memberId);
    }

    @PatchMapping("/negative/water")
    @Operation(summary = "부정양파 물 주기", description = """
      부정양파를 한 단계 성장시키는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<NegOnionDTO> waterNegOnion(
            HttpServletRequest request
    ){
        Long memberId = jwtProvider.getMemberId(request);
        return onionService.waterNegOnion(memberId);
    }

    @PostMapping("/grow/{isPos}")
    @Operation(summary = "양파 진화 정보 리턴", description = """
      양파의 진화 정보를 저장하고 보여주는 API입니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<EvolvedOnionDTO> evolveOnion(
            HttpServletRequest request,
            @PathVariable int isPos
    ){
        Long memberId = jwtProvider.getMemberId(request);
        boolean isPosBool = isPos == 1;
        return onionService.evolveOnion(memberId, isPosBool);
    }








}
