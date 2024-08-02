package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import site.lets_onion.lets_onionApp.dto.onionBook.OnionBookDTO;
import site.lets_onion.lets_onionApp.service.onionBook.OnionBookService;
import site.lets_onion.lets_onionApp.service.trade.TradeService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class OnionBookController {

    private final JwtProvider jwtProvider;
    private final OnionBookService onionBookService;
    private final TradeService tradeService;

    @GetMapping("")
    @Operation(summary = "양파 도감 조회", description = """
            친구 유저의 양파 도감을 조회하는 API입니다. 쿼리파라미터가 없으면 자신의 도감을 조회합니다. <br>
            1개 이상 모은 양파의 리스트와 상태메세지를 조회합니다.<br>
            **can_trade = true**인 경우 해당 양파는 개수가 2개 이상으로 교환이 가능합니다.
            """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<OnionBookDTO> getOnionBook(
            HttpServletRequest request,
            @Nullable @RequestParam(name = "member_id") Long memberId
    ) {
        Long id;
        if (memberId == null) {
            id = jwtProvider.getMemberId(request);
        } else {
            id = memberId;
        }
        return onionBookService.getCollectedOnionBook(id);
    }

}
