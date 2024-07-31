package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.lets_onion.lets_onionApp.service.onionBook.OnionBookService;
import site.lets_onion.lets_onionApp.service.trade.TradeService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;

@RestController
@RequiredArgsConstructor
@RequestMapping("/book")
public class OnionBookController {

    private final JwtProvider jwtProvider;
    private final OnionBookService onionBookService;
    private final TradeService tradeService;




}
