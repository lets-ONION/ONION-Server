package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;

import java.util.Optional;

public interface OnionBookRepository {

    //OnionBook id로 단일 조회
    OnionBook findById(Long id);

    //유저 OnionBook의 모든 도감 조회
    OnionBook findByMemberId(Long memberId);

    //도감 저장 (테스트용)
    OnionBook save(OnionBook onionBook);

}
