package site.lets_onion.lets_onionApp.service.onion;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;
import site.lets_onion.lets_onionApp.domain.calendar.MonthData;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.domain.onion.Onion;
import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.dto.onion.*;
import site.lets_onion.lets_onionApp.repository.calendar.DayRepository;
import site.lets_onion.lets_onionApp.repository.calendar.MonthRepository;
import site.lets_onion.lets_onionApp.repository.member.MemberRepository;
import site.lets_onion.lets_onionApp.repository.onion.GrowingOnionRepository;
import site.lets_onion.lets_onionApp.repository.onion.OnionHistoryRepository;
import site.lets_onion.lets_onionApp.repository.onionBook.OnionBookRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class OnionServiceImpl implements OnionService{

    private final MemberRepository memberRepository;
    private final MonthRepository monthRepository;
    private final DayRepository dayRepository;
    private final OnionHistoryRepository onionHistoryRepository;
    private final GrowingOnionRepository growingOnionRepository;
    private final OnionBookRepository onionBookRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseDTO<OnionsDTO> getMainPage(Long memberId) {
        boolean isSpoken = isSpoken(memberId);
        Member member = findMember(memberId);
        OnionsDTO onionsDTO = new OnionsDTO(member, isSpoken);
        return new ResponseDTO<>(onionsDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<OnionsDTO> saveOnionName(Long memberId, NamingOnionsDTO namingOnionsDTO) {
        Member member = findMember(memberId);
        String onionName = namingOnionsDTO.getOnionName();
        GrowingOnion growingOnion = member.getOnions();
        growingOnion.getPosOnion().updateOnionName(onionName);
        growingOnion.getNegOnion().updateOnionName(onionName);

        OnionsDTO onionsDTO = new OnionsDTO(member, false);
        return new ResponseDTO<>(onionsDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<Boolean> savePosNote(Long memberId, PosNoteDTO posNoteDTO) {
        checkAndCreateDay(memberId);
        DayData dayData = findToday(memberId);
        dayData.updatePosNote(posNoteDTO.getNote());
        return new ResponseDTO<>(true, Responses.NO_CONTENT);
    }

    @Override
    public ResponseDTO<PosOnionWithEvolvableDTO> waterPosOnion(Long memberId) {
        checkAndCreateDay(memberId);
        Member member = findMember(memberId);
        Onion posOnion = member.getOnions().getPosOnion();
        posOnion.waterOnion();

        PosOnionWithEvolvableDTO posOnionDTO = new PosOnionWithEvolvableDTO(posOnion);
        return new ResponseDTO<>(posOnionDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<NegOnionWithEvolvableDTO> waterNegOnion(Long memberId) {
        checkAndCreateDay(memberId);
        Member member = findMember(memberId);
        Onion negOnion = member.getOnions().getNegOnion();
        negOnion.waterOnion();
        NegOnionWithEvolvableDTO negOnionDTO = new NegOnionWithEvolvableDTO(negOnion);
        return new ResponseDTO<>(negOnionDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<EvolvedOnionDTO> evolveOnion(Long memberId, boolean isPos) {
        GrowingOnion growingOnion = growingOnionRepository.findByMemberId(memberId);
        Onion onion = (isPos) ? growingOnion.getPosOnion() : growingOnion.getNegOnion();
        if (onion.getGrowthStage() < 7){throw new CustomException(Exceptions.ONION_LEVEL_UNDER_7);}
        String onionName = onion.getName() + " " + onion.getGeneration() + "세";

        OnionType onionType = OnionType.randomType();
        DayData dayData = findToday(memberId);
        OnionHistory onionHistory = OnionHistory.builder()
                .dayData(dayData)
                .onionType(onionType)
                .nameAndGeneration(onionName)
                .build();
        onionHistoryRepository.save(onionHistory);

        OnionBook onionBook = onionBookRepository.findByMemberId(memberId);
        onionBook.getOnion(onionType).increaseQuantity();

        onion.nextOnion();
        EvolvedOnionDTO evolvedOnionDTO = new EvolvedOnionDTO(onionHistory);
        return new ResponseDTO<>(evolvedOnionDTO, Responses.OK);
    }

    /**
     * 예외를 처리하며 유저를 조회합니다.
     * @param memberId
     * @return
     */
    /*유저 조회*/
    private Member findMember(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() ->
                new CustomException(Exceptions.MEMBER_NOT_EXIST));
    }

    /**
     * 예외를 처리하며 오늘의 말하기 여부를 조회합니다.
     * @param memberId
     * @return
     */
    /*오늘의 말하기 여부 체크*/
    private boolean isSpoken(Long memberId){
        Optional<MonthData> month = monthRepository.findByMemberIdAndYearMonth(memberId, YearMonth.now());
        if (month.isEmpty()) {return false;}
        Optional<DayData> day = dayRepository.findByMonthIdAndLocalDate(month.get().getId(), LocalDate.now());
        return day.isPresent();
    }

    /**
     * 예외를 처리하며 오늘에 해당하는 Day 객체를 조회합니다.
     * @param memberId
     * @return
     */
    private DayData findToday(Long memberId){
        Optional<MonthData> month = monthRepository.findByMemberIdAndYearMonth(memberId, YearMonth.now());
        if (month.isEmpty()) { throw new CustomException(Exceptions.MONTH_NOT_EXIST); }
        Optional<DayData> day = dayRepository.findByMonthIdAndLocalDate(month.get().getId(), LocalDate.now());
        if (day.isEmpty()) {
            throw new CustomException(Exceptions.DAY_NOT_EXIST);
        }
        return day.get();
    }

    private void checkAndCreateDay(Long memberId){
        if (!isSpoken(memberId)){
            Optional<MonthData> month = monthRepository.findByMemberIdAndYearMonth(memberId, YearMonth.now());
            MonthData monthData;
            if (month.isEmpty()) {
                Member member = findMember(memberId);
                MonthData monthData1 = MonthData.builder()
                        .member(member).build();
                monthRepository.save(monthData1);
                monthData = monthData1;
            } else {monthData = month.get();}
            DayData dayData = DayData.builder()
                    .month(monthData)
                    .build();
            dayRepository.save(dayData);
        }
    }
}