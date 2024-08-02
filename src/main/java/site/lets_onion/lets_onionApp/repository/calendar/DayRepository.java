package site.lets_onion.lets_onionApp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;

public interface DayRepository extends JpaRepository<DayData, Long>, BaseDayRepository {
}
