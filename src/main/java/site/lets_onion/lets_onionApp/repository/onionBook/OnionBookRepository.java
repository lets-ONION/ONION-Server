package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;

import java.util.List;

public interface OnionBookRepository {

    //OnionBook id로 단일 조회
    OnionBook findById(Long id);

    //유저 OnionBook의 모든 도감 조회
    OnionBook findByMemberId(Long memberId);


}
