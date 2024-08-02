package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.dto.trade.ReceivedTradeDTO;
import site.lets_onion.lets_onionApp.dto.trade.SentTradeDTO;
import site.lets_onion.lets_onionApp.dto.trade.TradeRequestDTO;
import site.lets_onion.lets_onionApp.service.trade.TradeService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/trade")
public class TradeController {

    private final JwtProvider jwtProvider;
    private final TradeService tradeService;

    @GetMapping("/get/sent")
    @Operation(summary = "보낸 교환 요청 조회", description = "내가 보낸 교환 요청 리스트를 조회합니다. 대기(PENDING) 상태의 요청만 조회합니다.")
    @ApiResponse(responseCode = "200")
    public ResponseDTO<List<SentTradeDTO>> getSentTradeRequests(HttpServletRequest request) {
        Long memberId = jwtProvider.getMemberId(request);
        return tradeService.getSentTradeRequestList(memberId);
    }

    @GetMapping("/get/received")
    @Operation(summary = "받은 교환 요청 조회", description = "내가 받은 교환 요청 리스트를 조회합니다. 대기(PENDING) 상태의 요청만 조회합니다.")
    @ApiResponse(responseCode = "200")
    public ResponseDTO<List<ReceivedTradeDTO>> getReceivedTradeRequests(HttpServletRequest request) {
        Long memberId = jwtProvider.getMemberId(request);
        return tradeService.getReceivedTradeRequestList(memberId);
    }

    @PostMapping("/{receiverId}")
    @Operation(summary = "교환 요청 전송", description = "**receiverId**에 해당하는 유저에게 양파 교환 요청을 전송합니다.")
    @ApiResponse(responseCode = "201")
    public ResponseDTO<SentTradeDTO> postTradeRequest(
            HttpServletRequest request,
            @PathVariable("receiverId") Long toMemberId,
            @RequestBody TradeRequestDTO dto
    ) {
        Long fromMemberId = jwtProvider.getMemberId(request);
        return tradeService.sendRequest(fromMemberId, toMemberId, dto.getRequestOnion(), dto.getResponseOnion());
    }

    //교환 요청 수락.. 거절...
    @PatchMapping("/cancel/{tradeId}")
    @Operation(summary = "교환 요청 취소", description = "**tradeId**에 해당하는 요청을 보낸 유저가 요청을 취소합니다.")
    @ApiResponse(responseCode = "200")
    public ResponseDTO<Boolean> cancelTradeRequest(
            HttpServletRequest request,
            @PathVariable("tradeId") Long tradeId
    ) {
        return tradeService.cancelRequest(tradeId);
    }

    @PatchMapping("/accept/{tradeId}")
    @Operation(summary = "교환 요청 수락", description = """
        **tradeId**에 해당하는 요청을 받은 유저가 요청을 수락합니다.<br>
        만약 요청을 수락한 유저의 양파 수가 충분하지 않으면 에러가 발생합니다.
        """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<Boolean> acceptTradeRequest(
            HttpServletRequest request,
            @PathVariable("tradeId") Long tradeId
    ) {
        return tradeService.acceptRequest(tradeId);
    }

    @PatchMapping("/reject/{tradeId}")
    @Operation(summary = "교환 요청 거절", description = "**tradeId**에 해당하는 요청을 받은 유저가 요청을 거절합니다.")
    @ApiResponse(responseCode = "200")
    public ResponseDTO<Boolean> rejectTradeRequest(
            HttpServletRequest request,
            @PathVariable("tradeId") Long tradeId
    ) {
        return tradeService.rejectRequest(tradeId);
    }
}
