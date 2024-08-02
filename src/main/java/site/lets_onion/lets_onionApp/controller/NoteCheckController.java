package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.lets_onion.lets_onionApp.domain.onion.NegCheckResult;
import site.lets_onion.lets_onionApp.domain.onion.PosCheckResult;
import site.lets_onion.lets_onionApp.dto.onion.NoteRequestDTO;
import site.lets_onion.lets_onionApp.service.onion.CheckOnionService;
import site.lets_onion.lets_onionApp.service.onion.CheckOnionServiceImpl;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/main/check")
public class NoteCheckController {

    private final CheckOnionService checkOnionService;

    @PostMapping("/test")
    @Operation(summary = "chatgpt api 테스트용 api", description = """
            chatgpt 응답 결과를 반환하는 테스트 api입니다.<br>
            0 = 긍정, 1 = 부정, 2 = 판단불가
            """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<String> test(
            HttpServletRequest request,
            @RequestBody NoteRequestDTO dto
    ) {
        return checkOnionService.test(dto.getNote());
    }

    @PostMapping("/pos")
    @Operation(summary = "긍정 양파 일기 검증", description = """
            긍정 양파에 말한 일기를 검증한 결과를 반환합니다. 응답 명세는 다음과 같습니다.<br>
            **POSITIVE** : 검증 성공, 긍정 맞음<br>
            **NEGATIVE** : 검증 실패, 부정 감지<br>
            **INVALID** : 검증 실패, 판단할 수 없는 텍스트<br>
            **FAIL** : 검증 실패, GPT 응답이 잘못됨
            """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<PosCheckResult> checkPosOnion(
            HttpServletRequest request,
            @RequestBody NoteRequestDTO dto
            ) {
        return checkOnionService.checkPosNote(dto.getNote());
    }

    @PostMapping("/neg")
    @Operation(summary = "부정 양파 일기 검증", description = """
            부정 양파에 말한 일기를 검증한 결과를 반환합니다. 응답 명세는 다음과 같습니다.<br>
            **VALID** : 검증 성공, 타당한 텍스트<br>
            **INVALID** : 검증 실패, 판단할 수 없는 텍스트<br>
            **FAIL** : 검증 실패, GPT 응답이 잘못됨
            """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<NegCheckResult> checkNegOnion(
            HttpServletRequest request,
            @RequestBody NoteRequestDTO dto
    ) {
        return checkOnionService.checkNegNote(dto.getNote());
    }
}
