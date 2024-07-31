package site.lets_onion.lets_onionApp.service.trade;

import site.lets_onion.lets_onionApp.dto.trade.ReceivedTradeRequestDTO;
import site.lets_onion.lets_onionApp.dto.trade.SentTradeRequestDTO;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.util.List;

public interface TradeService {

    //보낸 교환 요청 리스트 조회
    ResponseDTO<List<SentTradeRequestDTO>> getSentTradeRequestList(Long memberId);

    //받은 교환 요청 리스트 조회
    ResponseDTO<List<ReceivedTradeRequestDTO>> getReceivedTradeRequestList(Long memberId);

    //교환 요청 생성
    ResponseDTO<SentTradeRequestDTO> sendRequest(Long fromMemberId, Long toMemberId, OnionType fromOnionType, OnionType toOnionType);

    //교환 요청 취소
    ResponseDTO<Boolean> cancelRequest(Long tradeId);

    //교환 요청 수락
    ResponseDTO<Boolean> acceptRequest(Long tradeId);

    //교환 요청 거절
    ResponseDTO<Boolean> refuseRequest(Long tradeId);
}
