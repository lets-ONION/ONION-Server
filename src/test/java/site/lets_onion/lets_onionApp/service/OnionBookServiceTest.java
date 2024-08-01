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
import site.lets_onion.lets_onionApp.dto.trade.SentTradeDTO;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.service.onionBook.OnionBookService;
import site.lets_onion.lets_onionApp.service.trade.TradeService;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class OnionBookServiceTest {

    @Autowired private OnionBookService onionBookService;
    @Autowired private TradeService tradeService;
    @Autowired private MemberRepository memberRepository;
    @Autowired private TestGenerator generator;


    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L).kakaoId(1L)
                .nickname("test_member")
                .build();
        testMember = memberRepository.save(testMember);
    }

    @Test
    @Transactional
    public void 교환_요청_보내기() throws Exception {
        //given
        Member friend = generator.createMember();


        //when
        SentTradeDTO response = tradeService
                .sendRequest(testMember.getId(), friend.getId(), "양파깡", "양파튀김")
                .getData();

        //then
        Assertions.assertThat(response.getRequestOnion()).isEqualTo("양파깡");
    }
}
