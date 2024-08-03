package site.lets_onion.lets_onionApp.repository.calendar;

import site.lets_onion.lets_onionApp.domain.calendar.DayData;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BaseDayRepository {

    /*Month와 날짜로 조회*/
    Optional<DayData> findByMonthIdAndLocalDate(Long monthId, LocalDate localDate);

    // Month로 조회
    List<DayData> findByMonthId(Long monthId);

}
