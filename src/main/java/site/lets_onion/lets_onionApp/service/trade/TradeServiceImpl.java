package site.lets_onion.lets_onionApp.service.trade;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionRepository;
import site.lets_onion.lets_onionApp.repository.trade.TradeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService{

    private final BaseMemberRepository memberRepository;
    private final OnionRepository onionRepository;
    private final TradeRepository tradeRepository;

    /**
     * 검증
     */
    public void validateOnionQuantity(Onion onion) {

        if (onion.getCollectedQuantity() <= 1) {
            throw new NotEnoughQuantityException("양파 개수가 부족합니다.");
        }
    }
    public void validateDuplicateRequest(TradeRequest tradeRequest) {

        if (tradeRequest.status != TradeStatus.PENDING) {
            throw new IllegalStateException("중복된 요청입니다");
        }
    }

    @Transactional
    @Override
    public Long sendRequest(Long fromMemberId, Long toMemberId, OnionType fromOnionType, OnionType toOnionType) {

        Member fromMember = memberRepository.findByMemberId(fromMemberId);
        Member toMember = memberRepository.findByMemberId(toMemberId);

        TradeRequest tradeRequest = TradeRequest.createTradeRequest(fromMember, toMember, fromOnion, toOnion);

        // 교환 요청한 유저의 교환 요청 양파 개수 -1
        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOniontype(fromMemberId, fromOnionType);
        validateOnionQuantity(fromOnionOfFromMember);
        fromOnionOfFromMember.decreaseQuantity(1); //이런애들 저장 안해도 되나?

        tradeRepository.save(tradeRequest);

        return tradeRequest.getId();
    }

    @Transactional
    @Override
    public void cancelRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.CANCEL); //저장 안해도 되나?

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOniontype(fromOnionMember.getId(), fromOnionType);
        fromOnionOfFromMember.increaseQuantity(1);

    }

    @Transactional
    @Override
    public void acceptRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.ACCEPT);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        OnionType toOnionType = tradeRequest.getToOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        Member toOnionMember = tradeRequest.getToMember();

        Onion toOnionOfToMember = onionRepository.findByMemberIdAndOniontype(toOnionMember.getId(), toOnionType);
        validateOnionQuantity(toOnionOfToMember);
        toOnionOfToMember.decreaseQuantity(1);

        Onion fromOnionOfToMember = onionRepository.findByMemberIdAndOniontype(toOnionMember.getId(), fromOnionType);
        fromOnionOfToMember.increaseQuantity(1);

        Onion toOnionOfFromMember = onionRepository.findByMemberIdAndOniontype(fromOnionMember.getId(), toOnionType);
        toOnionOfFromMember.increaseQuantity(1);

    }

    @Transactional
    @Override
    public void refuseRequest(Long tradeId) {

        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.REFUSE);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        Onion fromOnionOfFromMember = onionRepository.findByMemberIdAndOniontype(fromOnionMember.getId(), fromOnionType);
        fromOnionOfFromMember.increaseQuantity(1);

    }
}
