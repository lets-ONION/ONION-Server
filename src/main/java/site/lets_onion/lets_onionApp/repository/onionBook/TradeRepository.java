package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.TradeRequest;

import java.util.List;

public interface TradeRepository {

    //유저가 보낸 요청 전체 조회
    List<TradeRequest> findByFromMemberId(Long memberId);

    //유저가 받은 요청 전체 조회
    List<TradeRequest> findByToMemberId(Long memberId);

    //교환 요청 생성
    void save(TradeRequest tradeRequest);

}
