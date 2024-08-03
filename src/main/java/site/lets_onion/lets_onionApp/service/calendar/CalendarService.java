package site.lets_onion.lets_onionApp.service.calendar;

import site.lets_onion.lets_onionApp.dto.calendar.DailyDTO;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;

public interface CalendarService {

    // 특정 월에 진화한 양파 리스트 조회
    ResponseDTO<DailyDTO> getOnionHistoryByMonth(YearMonth yearMonth, Long memberId);

    // 특정 일에 작성된 긍정일기 조회
    ResponseDTO<PosNoteDTO> getPosNoteByDay(LocalDate localDate, Long memberId);

    // 특정 일에 작성된 긍정일기 수정
    ResponseDTO<PosNoteDTO> updatePosNoteByDay(LocalDate localDate, Long memberId, PosNoteDTO posNoteDTO);

    // 특정 일에 작성된 긍정일기 삭제
    ResponseDTO<Boolean> deletePosNoteByDay(LocalDate localDate, Long memberId);

}
