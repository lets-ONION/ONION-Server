package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.Onion;

public interface OnionRepository {

    //memberId의 유저의 onionType에 맞는 Onion 객체 조회
    Onion findByMemberIdAndOniontype(Long memberId, OnionType onionType);

}
