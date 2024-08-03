package site.lets_onion.lets_onionApp.service;

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
import site.lets_onion.lets_onionApp.repository.onion.OnionHistoryRepository;
import site.lets_onion.lets_onionApp.service.calendar.CalendarService;
import site.lets_onion.lets_onionApp.service.onion.OnionService;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

import java.time.LocalDate;
import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class CalendarServiceTest {

    @Autowired private MemberRepository memberRepository;
    @Autowired private MonthRepository monthRepository;
    @Autowired private DayRepository dayRepository;
    @Autowired private OnionHistoryRepository onionHistoryRepository;
    @Autowired private OnionService onionService;
    @Autowired private CalendarService calendarService;

    private Member testMember;

    @BeforeEach
    void setUp(){
        testMember = Member.builder()
                .id(1L).kakaoId(1L)
                .nickname("test_member")
                .build();
        testMember.getOnions().getPosOnion().updateOnionName("냥냥이");
        testMember.getOnions().getNegOnion().updateOnionName("냥냥이");
        testMember = memberRepository.save(testMember);
        onionService.waterPosOnion(testMember.getId());
    }

    @Test
    @Transactional
    public void 긍정일기_수정(){
        // given
        String originalPosNote = "원래";
        PosNoteDTO posNoteDTO = new PosNoteDTO(originalPosNote);
        onionService.savePosNote(testMember.getId(), posNoteDTO);

        // when
        String newPosNote = "수정";
        PosNoteDTO posNoteDTO1 = new PosNoteDTO(newPosNote);
        calendarService.updatePosNoteByDay(LocalDate.now(), testMember.getId(), posNoteDTO1);

        // then
        MonthData monthData = monthRepository.findByMemberIdAndYearMonth(testMember.getId(), YearMonth.now())
                .orElseThrow(() -> new CustomException(Exceptions.MONTH_NOT_EXIST));
        DayData dayData = dayRepository.findByMonthIdAndLocalDate(monthData.getId(), LocalDate.now())
                        .orElseThrow(() -> new CustomException(Exceptions.DAY_NOT_EXIST));
        assertEquals(newPosNote, dayData.getPosNote());
    }

    @Test
    @Transactional
    public void 긍정일기_삭제(){
        // given
        String posNote = "원래";
        PosNoteDTO posNoteDTO = new PosNoteDTO(posNote);
        onionService.savePosNote(testMember.getId(), posNoteDTO);

        // when
        calendarService.deletePosNoteByDay(LocalDate.now(), testMember.getId());

        // then
        MonthData monthData = monthRepository.findByMemberIdAndYearMonth(testMember.getId(), YearMonth.now())
                .orElseThrow(() -> new CustomException(Exceptions.MONTH_NOT_EXIST));
        DayData dayData = dayRepository.findByMonthIdAndLocalDate(monthData.getId(), LocalDate.now())
                .orElseThrow(() -> new CustomException(Exceptions.DAY_NOT_EXIST));
        assertNull(dayData.getPosNote());
    }


}
