package site.lets_onion.lets_onionApp.repository.trade;

import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;

import java.util.List;
import java.util.Optional;

public interface TradeRepository {

    //id에 맞는 요청 단일 조회
    TradeRequest findById(Long tradeId);

    //유저가 보낸 요청 전체 조회
    List<TradeRequest> findAllByFromMemberId(Long memberId);

    //유저가 받은 요청 전체 조회
    List<TradeRequest> findAllByToMemberId(Long memberId);

    //교환 요청 생성
    void save(TradeRequest tradeRequest);

}
