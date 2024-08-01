package site.lets_onion.lets_onionApp.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.TestGenerator;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.trade.TradeStatus;
import site.lets_onion.lets_onionApp.dto.trade.SentTradeDTO;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.repository.trade.TradeRepository;
import site.lets_onion.lets_onionApp.service.onionBook.OnionBookService;
import site.lets_onion.lets_onionApp.service.trade.TradeService;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class TradeServiceTest {

    @Autowired private OnionBookService onionBookService;
    @Autowired private TradeService tradeService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TestGenerator generator;
    @Autowired private OnionBookRepository onionBookRepository;
    @Autowired private TradeRepository tradeRepository;


    private Member member1, member2;
    private OnionBook member1OnionBook, member2OnionBook;
    private int member1GgangQuantity, member2GgangQuantity, member1FriedQuantity, member2FriedQuantity;

    @BeforeEach
    void setUp() {
        member1 = generator.createMember();
        member2 = generator.createMember();
        member1OnionBook = generator.createOnionBook(member1);
        member2OnionBook = generator.createOnionBook(member2);
        //교환 요청 : 양파깡, 교환 응답 : 양파튀김
        member1GgangQuantity = member1OnionBook.getOnionGgang().getCollectedQuantity();
        member2GgangQuantity = member2OnionBook.getOnionGgang().getCollectedQuantity();
        member1FriedQuantity = member1OnionBook.getOnionFried().getCollectedQuantity();
        member2FriedQuantity = member2OnionBook.getOnionFried().getCollectedQuantity();
    }

    @Test
    @Transactional
    public void 교환_요청_전송() throws Exception {
        //given
        //when
        SentTradeDTO response = tradeService
                .sendRequest(member1.getId(), member2.getId(), "양파깡", "양파튀김")
                .getData();
        //then
        Assertions.assertThat(response.getRequestOnion()).isEqualTo("양파깡");
        Assertions.assertThat(response.getResponseOnion()).isEqualTo("양파튀김");
        Assertions.assertThat(response.getResMember().getId()).isEqualTo(member2.getId());
        //교환 요청자 양파깡 개수 -1 체크
        Assertions.assertThat(member1OnionBook.getOnionGgang().getCollectedQuantity()).isEqualTo(member1GgangQuantity - 1);
    }

    @Test
    @Transactional
    public void 교환_요청_전송_예외() throws Exception {
        //given
        Member reqMember = generator.createMember();
        OnionBook reqMemberOnionBook = OnionBook.testBuilder()
                .member(reqMember)
                .ggang(1) //양파깡 개수 1개로 제한 //.ring(3).raw(3).pilled(3).fried(3).pickle(3).sushi(3).kimchi(3).soup(3).grilled(3)
                .build();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            tradeService.sendRequest(reqMember.getId(), member2.getId(), "양파깡", "양파튀김");
        });
    }

    @Test
    @Transactional
    public void 교환_요청_수락() throws Exception {
        //given
        SentTradeDTO response = tradeService
                .sendRequest(member1.getId(), member2.getId(), "양파깡", "양파튀김")
                .getData();
        Long tradeId = response.getId();
        //when
        tradeService.acceptRequest(tradeId);
        //then
        Assertions.assertThat(tradeRepository.findById(tradeId).getStatus()).isEqualTo(TradeStatus.ACCEPT);
        Assertions.assertThat(member1OnionBook.getOnionGgang().getCollectedQuantity()).isEqualTo(member1GgangQuantity - 1);
        Assertions.assertThat(member1OnionBook.getOnionFried().getCollectedQuantity()).isEqualTo(member1FriedQuantity + 1);
        Assertions.assertThat(member2OnionBook.getOnionGgang().getCollectedQuantity()).isEqualTo(member2GgangQuantity + 1);
        Assertions.assertThat(member2OnionBook.getOnionFried().getCollectedQuantity()).isEqualTo(member2FriedQuantity - 1);
    }

    @Test
    @Transactional
    public void 교환_요청_수락_예외() throws Exception {
        //given
        Member resMember = generator.createMember();
        OnionBook resMemberOnionBook = OnionBook.testBuilder()
                .member(resMember)
                .fried(1) //양파깡 개수 1개로 제한 //.ring(3).raw(3).pilled(3).ggang(3).pickle(3).sushi(3).kimchi(3).soup(3).grilled(3)
                .build();
        SentTradeDTO response = tradeService
                .sendRequest(member1.getId(), resMember.getId(), "양파깡", "양파튀김")
                .getData();
        Long tradeId = response.getId();
        //when
        //then
        assertThrows(IllegalArgumentException.class, () -> {
            tradeService.acceptRequest(tradeId);
        });
    }

    @Test
    @Transactional
    public void 교환_요청_거절() throws Exception {
        //given
        SentTradeDTO response = tradeService
                .sendRequest(member1.getId(), member2.getId(), "양파깡", "양파튀김")
                .getData();
        Long tradeId = response.getId();
        //when
        tradeService.rejectRequest(tradeId);
        //then
        Assertions.assertThat(tradeRepository.findById(tradeId).getStatus()).isEqualTo(TradeStatus.REJECT);
        Assertions.assertThat(member1OnionBook.getOnionGgang().getCollectedQuantity()).isEqualTo(member1GgangQuantity);
    }

    @Test
    @Transactional
    public void 교환_요청_취소() throws Exception {
        //given
        SentTradeDTO response = tradeService
                .sendRequest(member1.getId(), member2.getId(), "양파깡", "양파튀김")
                .getData();
        Long tradeId = response.getId();
        //when
        tradeService.cancelRequest(tradeId);
        //then
        Assertions.assertThat(tradeRepository.findById(tradeId).getStatus()).isEqualTo(TradeStatus.CANCEL);
        Assertions.assertThat(member1OnionBook.getOnionGgang().getCollectedQuantity()).isEqualTo(member1GgangQuantity);
    }
}
