package site.lets_onion.lets_onionApp.repository.trade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;

import java.util.List;

@Repository
public class TradeRepositoryImpl implements TradeRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public TradeRequest findById(Long tradeId) {
        return em.find(TradeRequest.class, tradeId);
    }

    @Override
    public List<TradeRequest> findListByFromMemberId(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " where t.fromMemberId = :memberId",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<TradeRequest> findListByToMemberId(Long memberId) {
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
