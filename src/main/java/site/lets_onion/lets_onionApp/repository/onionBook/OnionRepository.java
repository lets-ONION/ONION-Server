package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.Onion;

public interface OnionRepository {

    //유저의 OnionType에 맞는 Onion 조회
    Onion findByMemberIdAndOniontype(Long memberId, OnionType onionType);

}
