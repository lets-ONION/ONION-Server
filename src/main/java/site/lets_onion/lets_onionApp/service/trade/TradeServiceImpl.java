package site.lets_onion.lets_onionApp.service.trade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.dto.trade.ReceivedTradeRequestDTO;
import site.lets_onion.lets_onionApp.dto.trade.SentTradeRequestDTO;
import site.lets_onion.lets_onionApp.repository.member.BaseMemberRepository;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.repository.trade.TradeRepository;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 보낸 교환 요청 리스트를 조회합니다.
     * @param memberId
     * @return
     */
    @Override
    public ResponseDTO<List<SentTradeRequestDTO>> getSentTradeRequestList(Long memberId) {
        List<TradeRequest> findRequests = tradeRepository.findAllByFromMemberId(memberId);
        List<SentTradeRequestDTO> sentTradeRequests = findRequests.stream()
                .map(tr -> new SentTradeRequestDTO(tr))
                .collect(Collectors.toList());
        return new ResponseDTO<>(
                sentTradeRequests,
                Responses.OK
        );
    }

    /**
     * 받은 교환 요청 리스트를 조회합니다.
     * @param memberId
     * @return
     */
    @Override
    public ResponseDTO<List<ReceivedTradeRequestDTO>> getReceivedTradeRequestList(Long memberId) {
        List<TradeRequest> findRequests = tradeRepository.findAllByToMemberId(memberId);
        List<ReceivedTradeRequestDTO> receivedTradeRequests = findRequests.stream()
                .map(tr -> new ReceivedTradeRequestDTO(tr))
                .collect(Collectors.toList());
        return new ResponseDTO<>(
                receivedTradeRequests,
                Responses.OK
        );
    }

    /**
     * 교환 요청을 전송합니다
     * @param fromMemberId
     * @param toMemberId
     * @param fromOnionType
     * @param toOnionType
     * @return
     */
    @Override
    @Transactional
    public ResponseDTO<SentTradeRequestDTO> sendRequest(Long fromMemberId, Long toMemberId, OnionType fromOnionType, OnionType toOnionType) {
        Member fromMember = memberRepository.findByMemberId(fromMemberId);
        Member toMember = memberRepository.findByMemberId(toMemberId);
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromMemberId);
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);

        //남은 양파 개수 검증
        validateOnionQuantity(fromOnionOfFromMember);

        //교환요청 객체 생성
        TradeRequest tradeRequest = TradeRequest.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .fromOnionType(fromOnionType)
                .toOnionType(toOnionType)
                .build();
        tradeRepository.save(tradeRequest);

        // 교환 요청한 유저의 교환 요청 양파 개수 -1
        fromOnionOfFromMember.decreaseQuantity();

        return new ResponseDTO<>(
                new SentTradeRequestDTO(tradeRequest),
                Responses.CREATED);
    }

    /**
     * 교환 요청을 취소합니다
     * @param tradeId
     */
    @Override
    @Transactional
    public ResponseDTO<Boolean> cancelRequest(Long tradeId) {
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        //중복 요청 검증
        validateDuplicateRequest(tradeRequest);

        //교환 상태를 CANCEL로 바꿈.
        tradeRequest.updateStatus(TradeStatus.CANCEL);

        //교환 신청한 유저의 교환 요청 양파 개수 원복
        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

        return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
    }

    /**
     * 교환 요청을 수락합니다
     * 양파 개수가 부족하다면 교환을 취소시킵니다.
     * @param tradeId
     */
    @Override
    @Transactional
    public ResponseDTO<Boolean> acceptRequest(Long tradeId) {
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        //중복 요청 검증
        validateDuplicateRequest(tradeRequest);

        //교환 상태를 ACCEPT로 바꿈.
        tradeRequest.updateStatus(TradeStatus.ACCEPT);

        //교환 성사된 유저들의 교환 양파 개수 조정
        OnionType fromOnionType = tradeRequest.getFromOnion();
        OnionType toOnionType = tradeRequest.getToOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        Member toOnionMember = tradeRequest.getToMember();
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        OnionBook toMemberOnionBook = onionBookRepository.findByMemberId(toOnionMember.getId());

        Onion toOnionOfToMember = toMemberOnionBook.getOnion(toOnionType);
        validateOnionQuantity(toOnionOfToMember); //남은 양파 개수 검증
        toOnionOfToMember.decreaseQuantity();

        Onion fromOnionOfToMember = toMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfToMember.increaseQuantity();

        Onion toOnionOfFromMember = fromMemberOnionBook.getOnion(toOnionType);
        toOnionOfFromMember.increaseQuantity();

        return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
    }

    /**
     * 교환 요청을 거절합니다.
     * @param tradeId
     */
    @Override
    @Transactional
    public ResponseDTO<Boolean> refuseRequest(Long tradeId) {
        TradeRequest tradeRequest = tradeRepository.findById(tradeId);

        //중복 요청 검증
        validateDuplicateRequest(tradeRequest);

        //교환 상태를 REJECT로 바꿈
        tradeRequest.updateStatus(TradeStatus.REJECT);

        //교환 신청한 유저의 교환 요청 양파 개수 원복
        OnionType fromOnionType = tradeRequest.getFromOnion();
        Member fromOnionMember = tradeRequest.getFromMember();
        OnionBook fromMemberOnionBook = onionBookRepository.findByMemberId(fromOnionMember.getId());
        Onion fromOnionOfFromMember = fromMemberOnionBook.getOnion(fromOnionType);
        fromOnionOfFromMember.increaseQuantity();

        return new ResponseDTO<>(Boolean.TRUE, Responses.OK);
    }
}
