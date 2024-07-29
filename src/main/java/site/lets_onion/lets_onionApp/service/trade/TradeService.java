package site.lets_onion.lets_onionApp.service.trade;

import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;

public interface TradeService {

    //교환 요청 생성
    Long sendRequest(Long fromMemberId, Long toMemberId, OnionType fromOnionType, OnionType toOnionType);

    //교환 요청 취소
    void cancelRequest(Long tradeId);

    //교환 요청 수락
    void acceptRequest(Long tradeId);

    //교환 요청 거절
    void refuseRequest(Long tradeId);
}
