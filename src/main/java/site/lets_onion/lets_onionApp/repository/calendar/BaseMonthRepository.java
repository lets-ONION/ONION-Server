package site.lets_onion.lets_onionApp.repository.calendar;

import site.lets_onion.lets_onionApp.domain.calendar.MonthData;

import java.time.YearMonth;
import java.util.Optional;

public interface BaseMonthRepository {

    /*멤버 아이디와 월 기준 조회*/
    Optional<MonthData> findByMemberIdAndYearMonth(Long memberId, YearMonth yearMonth);


}
