package site.lets_onion.lets_onionApp.repository.onion;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onion.GrowingOnion;
import site.lets_onion.lets_onionApp.util.exception.CustomException;
import site.lets_onion.lets_onionApp.util.exception.Exceptions;

import java.util.Optional;

@Repository
public class BaseGrowingOnionRepositoryImpl implements BaseGrowingOnionRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public GrowingOnion findByMemberId(Long memberId) {
        return em.createQuery("select g from GrowingOnion g" +
                " where g.member.id =:memberId", GrowingOnion.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }
}
