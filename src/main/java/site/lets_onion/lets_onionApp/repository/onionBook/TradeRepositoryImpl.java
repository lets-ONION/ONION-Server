package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.TradeRequest;

import java.util.List;

@Repository
public class TradeRepositoryImpl implements TradeRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<TradeRequest> findByFromMemberId(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " where t.fromMemberId = :memberId",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<TradeRequest> findByToMemberId(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " where t.toMemberId = :memberId",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public void save(TradeRequest tradeRequest) {
        em.persist(tradeRequest);
    }

}
