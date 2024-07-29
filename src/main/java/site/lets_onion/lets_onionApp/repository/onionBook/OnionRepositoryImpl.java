package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;

@Repository
public class OnionRepositoryImpl implements OnionRepository{

    @PersistenceContext
    private EntityManager em;


    @Override
    public Onion findByMemberIdAndOniontype(Long memberId, OnionType onionType) {
        return em.createQuery("select ob." + onionType + " from OnionBook ob" //jpql 잘 날아가는지 확인 //안되면 ob.field.onionType = onionType인 필드 선택하게.
                                + " where ob.member = :memberId",
                        Onion.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }
}
