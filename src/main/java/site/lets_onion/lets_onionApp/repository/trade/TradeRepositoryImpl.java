package site.lets_onion.lets_onionApp.repository.trade;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.trade.TradeRequest;

import java.util.List;
import java.util.Optional;

@Repository
public class TradeRepositoryImpl implements TradeRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public TradeRequest findById(Long tradeId) {
        return em.find(TradeRequest.class, tradeId);
    }

    @Override
    public List<TradeRequest> findAllByFromMemberIdIfPending(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " left join fetch t.fromMember fm"
                        + " where fm.id = :memberId"
                        + " and t.status = 'PENDING'",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<TradeRequest> findAllByToMemberIdIfPending(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " left join fetch t.toMember tm"
                        + " where tm.id = :memberId"
                        + " and t.status = 'PENDING'",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public void save(TradeRequest tradeRequest) {
        em.persist(tradeRequest);
    }

}
