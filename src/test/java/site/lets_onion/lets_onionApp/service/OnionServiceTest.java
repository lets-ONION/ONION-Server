package site.lets_onion.lets_onionApp.service.onion;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;
import site.lets_onion.lets_onionApp.domain.calendar.MonthData;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.repository.calendar.DayRepository;
import site.lets_onion.lets_onionApp.repository.calendar.MonthRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.repository.onion.GrowingOnionRepository;
import site.lets_onion.lets_onionApp.repository.onion.OnionHistoryRepository;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class OnionServiceTest {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MonthRepository monthRepository;
    @Autowired
    private DayRepository dayRepository;
    @Autowired
    private GrowingOnionRepository growingOnionRepository;
    @Autowired
    private OnionHistoryRepository onionHistoryRepository;
    @Autowired
    private OnionService onionService;

    private Member testMember;

    @BeforeEach
    void setUp() {
        testMember = Member.builder()
                .id(1L).kakaoId(1L)
                .nickname("test_member")
                .build();
        testMember.getOnions().createOnions("냥냥이", "멍멍이");
        testMember = memberRepository.save(testMember);
    }

    @Test
    @Transactional
    public void 긍정일기_저장(){

        // given
        String posNote = "뿌듯한 하루";

        // when
        PosNoteDTO posNoteDTO = new PosNoteDTO(posNote);
        onionService.savePosNote(testMember.getId(), posNoteDTO);

        // then
        Optional<MonthData> month = monthRepository.findByMemberIdAndYearMonth(testMember.getId(), YearMonth.now());
        Optional<DayData> day = dayRepository.findByMonthIdAndLocalDate(month.get().getId(), LocalDate.now());
        assertEquals(day.get().getPosNote(), posNote);
    }

    @Test
    @Transactional
    public void 긍정양파_물_주기(){

        // given
        int level = testMember.getOnions().getPosOnion().getGrowthStage();

        // when
        onionService.waterPosOnion(testMember.getId());

        // then
        assertEquals(level+1, testMember.getOnions().getPosOnion().getGrowthStage());

    }

    @Test
    @Transactional
    public void 부정양파_물_주기(){
        // given
        int level = testMember.getOnions().getNegOnion().getGrowthStage();

        // when
        onionService.waterNegOnion(testMember.getId());

        // then
        assertEquals(level+1, testMember.getOnions().getNegOnion().getGrowthStage());
    }

    @Test @Transactional
    public void 양파_진화(){
        // given
        String onionName = testMember.getOnions().getPosOnion().getName() + " " + testMember.getOnions().getPosOnion().getGeneration() + "세";
        MonthData month = MonthData.builder()
                .member(testMember)
                .build();
        monthRepository.save(month);
        DayData dayData = DayData.builder()
                .month(month)
                .build();
        dayRepository.save(dayData);

        // when
        onionService.evolveOnion(testMember.getId(), true);

        // then
        assertEquals(onionName, onionHistoryRepository.findAll().get(0).getNameAndGeneration());
    }
}