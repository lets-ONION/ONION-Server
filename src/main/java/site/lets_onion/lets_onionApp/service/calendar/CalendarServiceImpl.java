package site.lets_onion.lets_onionApp.service.calendar;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;
import site.lets_onion.lets_onionApp.domain.calendar.MonthData;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;
import site.lets_onion.lets_onionApp.dto.calendar.DailyDTO;
import site.lets_onion.lets_onionApp.dto.calendar.DailyEvolvedOnionsDTO;
import site.lets_onion.lets_onionApp.dto.calendar.OnionTypeDTO;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.repository.calendar.DayRepository;
import site.lets_onion.lets_onionApp.repository.calendar.MonthRepository;
import site.lets_onion.lets_onionApp.repository.onion.OnionHistoryRepository;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;
import site.lets_onion.lets_onionApp.util.response.Responses;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@Transactional
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService{

    private final MonthRepository monthRepository;
    private final DayRepository dayRepository;
    private final OnionHistoryRepository onionHistoryRepository;

    @Override
    public ResponseDTO<DailyDTO> getOnionHistoryByMonth(YearMonth yearMonth, Long memberId) {
        MonthData monthData = findMonth(memberId, yearMonth);
        List<DayData> dayDataList = dayRepository.findByMonthId(monthData.getId());
        List<DailyEvolvedOnionsDTO> dailyEvolvedOnionsDTOS = new ArrayList<>();
        dayDataList.forEach(dayData -> {
            List<OnionTypeDTO> onionTypeDTOS = new ArrayList<>();
            List<OnionHistory> onionHistories = onionHistoryRepository.findByDayData(dayData.getId());
            onionHistories.forEach(onionHistory -> onionTypeDTOS.add(new OnionTypeDTO(onionHistory)));
            DailyEvolvedOnionsDTO dto = new DailyEvolvedOnionsDTO(dayData, onionTypeDTOS);
            dailyEvolvedOnionsDTOS.add(dto);
        });
        DailyDTO dailyDTO = new DailyDTO(dailyEvolvedOnionsDTOS);
        return new ResponseDTO<>(dailyDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<PosNoteDTO> getPosNoteByDay(LocalDate localDate, Long memberId) {
        MonthData monthData = findMonth(memberId, YearMonth.from(localDate));
        DayData dayData = findDay(monthData.getId(), localDate);
        PosNoteDTO posNoteDTO = new PosNoteDTO(dayData.getPosNote());
        return new ResponseDTO<>(posNoteDTO, Responses.OK);
    }

    @Override
    public ResponseDTO<PosNoteDTO> updatePosNoteByDay(LocalDate localDate, Long memberId, PosNoteDTO posNoteDTO) {
        MonthData monthData = findMonth(memberId, YearMonth.from(localDate));
        DayData dayData = findDay(monthData.getId(), localDate);
        dayData.updatePosNote(posNoteDTO.getNote());
        PosNoteDTO posNoteDTO1 = new PosNoteDTO(dayData.getPosNote());
        return new ResponseDTO<>(posNoteDTO1, Responses.OK);
    }

    @Override
    public ResponseDTO<Boolean> deletePosNoteByDay(LocalDate localDate, Long memberId) {
        MonthData monthData = findMonth(memberId, YearMonth.from(localDate));
        DayData dayData = findDay(monthData.getId(), localDate);
        dayData.deletePosNote();
        return new ResponseDTO<>(true, Responses.NO_CONTENT);
    }

    /**
     * 예외를 처리하며 월 데이터를 조회합니다.
     * @param memberId
     * @param yearMonth
     * @return
     */
    /*월 데이터 조회*/
    private MonthData findMonth(Long memberId, YearMonth yearMonth){
        return monthRepository.findByMemberIdAndYearMonth(memberId, yearMonth).orElseThrow(() ->
                new CustomException(Exceptions.MONTH_NOT_EXIST));
    }

    /**
     * 예외를 처리하며 일 데이터를 조회합니다.
     * @param monthId
     * @param localDate
     * @return
     */
    /*일 데이터 조회*/
    private DayData findDay(Long monthId, LocalDate localDate){
        return dayRepository.findByMonthIdAndLocalDate(monthId, localDate).orElseThrow(() ->
                new CustomException(Exceptions.DAY_NOT_EXIST));
    }
}
