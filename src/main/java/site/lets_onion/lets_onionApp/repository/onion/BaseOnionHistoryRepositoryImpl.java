package site.lets_onion.lets_onionApp.repository.onion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.calendar.DayData;
import site.lets_onion.lets_onionApp.domain.calendar.OnionHistory;

import java.util.List;

@Repository
public class BaseOnionHistoryRepositoryImpl implements BaseOnionHistoryRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<OnionHistory> findByDayData(Long dayDataId) {
        return em.createQuery("select o from OnionHistory o"
        + " where o.dayData.id =:dayDataId", OnionHistory.class)
                .setParameter("dayDataId", dayDataId)
                .getResultList();

    }
}
