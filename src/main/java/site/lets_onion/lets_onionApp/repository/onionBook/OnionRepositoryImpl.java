package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;

import java.util.List;

@Repository
public class OnionRepositoryImpl implements OnionRepository{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Onion findByMemberIdAndOnionType(Long memberId, OnionType onionType) {
        return em.createQuery("select ob." + onionType + " from OnionBook ob" //jpql 잘 날아가는지 확인
                                + " where ob.member = :memberId",
                        Onion.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }
}
