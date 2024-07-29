package site.lets_onion.lets_onionApp.service.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;

public interface OnionBookService {

    // 유저 도감 조회
    OnionBook findByMemberId(Long memberId);
}
