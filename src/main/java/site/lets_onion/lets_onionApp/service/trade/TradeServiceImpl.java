package site.lets_onion.lets_onionApp.service.trade;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.repository.trade.TradeRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService{

    private final BaseMemberRepository memberRepository;
    private final TradeRepository tradeRepository;
    private final OnionBookRepository onionBookRepository;

    /**
     * 검증
     */
    // 교환 가능한지 체크
    public void validateOnionQuantity(Onion onion) {

        if (onion.getCollectedQuantity() <= 1) {
            throw new NotEnoughQuantityException("양파 개수가 부족합니다.");
        }
    }
    //중복된 요청 체크
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
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromMemberId);
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);

        validateOnionQuantity(fromOnionOfFromMember);
        fromOnionOfFromMember.decreaseQuantity();

        tradeRepository.save(tradeRequest);

        return tradeRequest.getId();
    }

    @Override
    @Transactional
    public void cancelRequest(Long tradeId) {
        //엔티티 조회
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.CANCEL);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        //교환 신청한 유저의 교환 요청 양파 개수 원복
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

    }

    @Override
    @Transactional
    public void acceptRequest(Long tradeId) {
        //엔티티 조회
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.ACCEPT);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        OnionType toOnionType = tradeRequest.getToOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        Member toOnionMember = tradeRequest.getToMember();

        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        OnionBook toMemberOnionBook = onionBookRepository.findByMemberId(toOnionMember.getId());

        Onion toOnionOfToMember = toMemberOnionBook.getOnion(toOnionType);
        validateOnionQuantity(toOnionOfToMember);
        toOnionOfToMember.decreaseQuantity();

        Onion fromOnionOfToMember = toMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfToMember.increaseQuantity();

        Onion toOnionOfFromMember = fromMemberOnionBook.getOnion(toOnionType);
        toOnionOfFromMember.increaseQuantity();

    }

    @Override
    @Transactional
    public void refuseRequest(Long tradeId) {
        //엔티티 조회
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        validateDuplicateRequest(tradeRequest);
        tradeRequest.updateStatus(TradeStatus.REJECT);

        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();

        //교환 신청한 유저의 교환 요청 양파 개수 원복
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

    }
}
