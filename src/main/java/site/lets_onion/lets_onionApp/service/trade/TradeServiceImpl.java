package site.lets_onion.lets_onionApp.service.trade;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionRepository;
import site.lets_onion.lets_onionApp.repository.trade.TradeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService{

    private final BaseMemberRepository memberRepository;
    private final OnionRepository onionRepository;
    private final TradeRepository tradeRepository;
    private final OnionBookRepository onionBookRepository;

    /**
     * 검증
     */
    public void validateOnionQuantity(Onion onion) {

        if (onion.getCollectedQuantity() <= 1) {
            throw new NotEnoughQuantityException("양파 개수가 부족합니다.");
        }
    }
    public void validateDuplicateRequest(TradeRequest tradeRequest) {

        if (tradeRequest.getStatus() != TradeStatus.PENDING) {
            throw new IllegalStateException("중복된 요청입니다");
        }
    }

    @Override
    @Transactional
    public Long sendRequest(Long fromMemberId, Long toMemberId, OnionType fromOnionType, OnionType toOnionType) {

        Member fromMember = memberRepository.findByMemberId(fromMemberId);
        Member toMember = memberRepository.findByMemberId(toMemberId);

        TradeRequest tradeRequest = new TradeRequest(fromMember, toMember, fromOnionType, toOnionType);

        // 교환 요청한 유저의 교환 요청 양파 개수 -1
        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOnionType(fromMemberId, fromOnionType);
        validateOnionQuantity(fromOnionOfFromMember);
        fromOnionOfFromMember.decreaseQuantity(); //이런애들 저장 안해도 되나?

        tradeRepository.save(tradeRequest);

        return tradeRequest.getId();
    }

    @Override
    @Transactional
    public void cancelRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.CANCEL); //저장 안해도 되나?

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOnionType(fromOnionMember.getId(), fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

    }

    @Override
    @Transactional
    public void acceptRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.ACCEPT);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        OnionType toOnionType = tradeRequest.getToOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        Member toOnionMember = tradeRequest.getToMember();

        Onion toOnionOfToMember = onionRepository.findByMemberIdAndOnionType(toOnionMember.getId(), toOnionType);
        validateOnionQuantity(toOnionOfToMember);
        toOnionOfToMember.decreaseQuantity();

        Onion fromOnionOfToMember = onionRepository.findByMemberIdAndOnionType(toOnionMember.getId(), fromOnionType);
        fromOnionOfToMember.increaseQuantity();

        Onion toOnionOfFromMember = onionRepository.findByMemberIdAndOnionType(fromOnionMember.getId(), toOnionType);
        toOnionOfFromMember.increaseQuantity();

    }

    @Override
    @Transactional
    public void refuseRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.REJECT);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOnionType(fromOnionMember.getId(), fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

    }
}
