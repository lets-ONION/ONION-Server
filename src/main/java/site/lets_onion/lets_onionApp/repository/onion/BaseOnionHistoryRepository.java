package site.lets_onion.lets_onionApp.repository.onion;

import site.lets_onion.lets_onionApp.domain.calendar.DayData;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;

import java.util.List;

public interface BaseOnionHistoryRepository {

    // 날짜 기준으로 진화한 양파 조회
    List<OnionHistory> findByDayData(Long dayDataId);

}
