package site.lets_onion.lets_onionApp.repository.calendar;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.calendar.MonthData;

public interface MonthRepository extends JpaRepository<MonthData, Long>, BaseMonthRepository {
}
