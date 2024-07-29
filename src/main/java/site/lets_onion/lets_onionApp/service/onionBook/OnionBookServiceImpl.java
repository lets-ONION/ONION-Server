package site.lets_onion.lets_onionApp.service.onionBook;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OnionBookServiceImpl implements OnionBookService{

    private final OnionBookRepository onionBookRepository;

    @Override
    public OnionBook findByMemberId(Long memberId) {
        return onionBookRepository.findByMemberId(memberId);
    }
}
