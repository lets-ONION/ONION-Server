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
    public List<TradeRequest> findAllByFromMemberId(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " left fetch join t.fromMember fm"
                        + " where fm.id = :memberId",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public List<TradeRequest> findAllByToMemberId(Long memberId) {
        return em.createQuery("select t from TradeRequest t"
                        + " left fetch join t.toMember tm"
                        + " where tm.id = :memberId",
                        TradeRequest.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    @Override
    public void save(TradeRequest tradeRequest) {
        em.persist(tradeRequest);
    }

}
