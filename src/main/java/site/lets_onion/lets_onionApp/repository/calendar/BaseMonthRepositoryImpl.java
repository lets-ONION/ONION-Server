package site.lets_onion.lets_onionApp.repository.calendar;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.calendar.MonthData;

import java.time.YearMonth;
import java.util.Optional;

@Repository
public class BaseMonthRepositoryImpl implements BaseMonthRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Optional<MonthData> findByMemberIdAndYearMonth(Long memberId, YearMonth yearMonth) {
        try {
            return Optional.ofNullable(em.createQuery("select m from MonthData m"
                    + " where m.member.id =:memberId and m.monthValue =:yearMonth", MonthData.class)
                    .setParameter("memberId", memberId)
                    .setParameter("yearMonth", yearMonth)
                    .getSingleResult());
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}
