package site.lets_onion.lets_onionApp.repository.calendar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public class BaseDayRepositoryImpl implements BaseDayRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<DayData> findByMonthIdAndLocalDate(Long monthId, LocalDate localDate) {
        try {
            return Optional.ofNullable(em.createQuery("select d from DayData d"
                            + " where d.month.id =:monthId and d.dayValue =:localDate", DayData.class)
                    .setParameter("monthId", monthId)
                    .setParameter("localDate", localDate)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
