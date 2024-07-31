package site.lets_onion.lets_onionApp.repository.onion;

import org.springframework.data.jpa.repository.JpaRepository;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;

public interface OnionHistoryRepository extends JpaRepository<OnionHistory, Long> {

}
