package site.lets_onion.lets_onionApp.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import site.lets_onion.lets_onionApp.domain.member.Member;
import site.lets_onion.lets_onionApp.dto.calendar.DailyDTO;
import site.lets_onion.lets_onionApp.dto.calendar.PosNoteDTO;
import site.lets_onion.lets_onionApp.service.calendar.CalendarService;
import site.lets_onion.lets_onionApp.util.jwt.JwtProvider;
import site.lets_onion.lets_onionApp.util.response.ResponseDTO;

import java.time.LocalDate;
import java.time.YearMonth;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calendar")
public class CalendarController {

    private final CalendarService calendarService;
    private final JwtProvider jwtProvider;


    @GetMapping("")
    @Operation(summary = "달력 조회", description = """
      달력을 조회하는 API입니다.<br>
      특정 날짜에 양파를 진화시켰을 경우 해당 양파 이름과 이미지url을 리턴합니다.<br>
      특정 월의 정보를 조회하고자 할 경우 yearmonth 쿼리파라미터에 yyyy-MM 형태로 작성하면 됩니다.<br>
      ex. 2024-07
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<DailyDTO> getCalendar(
            HttpServletRequest request,
            @RequestParam(name = "yearmonth", required = false) YearMonth yearMonthParam
    ) {
        Long memberId = jwtProvider.getMemberId(request);
        YearMonth yearMonth;
        if (yearMonthParam == null) {yearMonth = YearMonth.now();}
        else {yearMonth = yearMonthParam;}
        return calendarService.getOnionHistoryByMonth(yearMonth, memberId);
    }

    @GetMapping("/{date}")
    @Operation(summary = "긍정일기 조회", description = """
      특정 날짜에 작성된 긍정일기를 조회하는 API입니다.<br>
      date : 2024-07-31 형식으로 조회하면 됩니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<PosNoteDTO> getPosNote(
            HttpServletRequest request,
            @PathVariable(name = "date") LocalDate localDate
            )
    {
        Long memberId = jwtProvider.getMemberId(request);
        return calendarService.getPosNoteByDay(localDate, memberId);
    }

    @PutMapping("/{date}")
    @Operation(summary = "긍정일기 수정", description = """
      특정 날짜에 작성된 긍정일기를 수정하는 API입니다.<br>
      date : 2024-07-31 형식으로 조회하면 됩니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<PosNoteDTO> updatePosNote(
            HttpServletRequest request,
            @PathVariable(name = "date") LocalDate localDate,
            @RequestBody PosNoteDTO posNoteDTO
    )
    {
        Long memberId = jwtProvider.getMemberId(request);
        return calendarService.updatePosNoteByDay(localDate, memberId, posNoteDTO);
    }

    @DeleteMapping("/{date}")
    @Operation(summary = "긍정일기 삭제", description = """
      특정 날짜에 작성된 긍정일기를 삭제하는 API입니다.<br>
      date : 2024-07-31 형식으로 조회하면 됩니다.
      """)
    @ApiResponse(responseCode = "200")
    public ResponseDTO<Boolean> deletePosNote(
            HttpServletRequest request,
            @PathVariable(name = "date") LocalDate localDate
    )
    {
        Long memberId = jwtProvider.getMemberId(request);
        return calendarService.deletePosNoteByDay(localDate, memberId);
    }


}
