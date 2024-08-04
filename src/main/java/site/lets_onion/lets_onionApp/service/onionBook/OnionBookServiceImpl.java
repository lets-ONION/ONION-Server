package site.lets_onion.lets_onionApp.service.onionBook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.onionBook.CollectedOnion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;
import site.lets_onion.lets_onionApp.dto.onionBook.OnionBookDTO;
import site.lets_onion.lets_onionApp.dto.onionBook.OnionDTO;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.util.redis.ServiceRedisConnector;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OnionBookServiceImpl implements OnionBookService{

    private final OnionBookRepository onionBookRepository;
    private final ServiceRedisConnector serviceRedisConnector; //상태메세지 조회 위해서

    /**
     * 도감을 조회합니다.
     * 1개 이상 모인 양파 리스트와 상태메세지를 조회합니다.
     * @param memberId
     * @return
     */
    @Override
    public ResponseDTO<OnionBookDTO> getCollectedOnionBook(Long memberId) {
        OnionBook onionBook = onionBookRepository.findByMemberId(memberId);

        //1개 이상 모인 양파 리스트
        List<OnionDTO> onionsCollected = new ArrayList<>();
        for (OnionType onionType: OnionType.values()) { //map으로 변환하기!
            CollectedOnion collectedOnion = onionBook.getOnion(onionType);
            if (collectedOnion.getCollectedQuantity() == 0) {
                continue;
            }
            onionsCollected.add(new OnionDTO(onionType.getOnionName(), onionType.getImageUrl(), collectedOnion.getCollectedQuantity()));
        }

        //상태메세지
        System.out.println("message !!");

        String message = "";
        try {
            message = serviceRedisConnector.getStatusMessage(memberId.toString());
        } catch (Exception e) {
            message = "";
        }

        return new ResponseDTO<>(
                new OnionBookDTO(onionsCollected, message),
                Responses.OK);
    }
}
