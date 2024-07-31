package site.lets_onion.lets_onionApp.service.onionBook;

import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.dto.onionBook.OnionBookDTO;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

public interface OnionBookService {

    // 유저 도감의 모은 양파들 조회
    ResponseDTO<OnionBookDTO> getCollectedOnionBook(Long memberId);

}
