package site.lets_onion.lets_onionApp.repository.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;

public interface OnionBookRepository {

    //유저의 OnionBook 조회
    OnionBook findById(Long id);

}
