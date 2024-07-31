package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.Onion;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;

import java.util.List;

@Repository
public class OnionBookRepositoryImpl implements OnionBookRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public OnionBook findById(Long id) { //Onion 객체들까지 다 불러오는지 봐야함
        return em.createQuery("select ob from OnionBook ob"
                                + " left fetch join ob.onionGgang"
                                + " left fetch join ob.onionRing"
                                + " left fetch join ob.onionRaw"
                                + " left fetch join ob.onionPilled"
                                + " left fetch join ob.onionFried"
                                + " left fetch join ob.onionPickle"
                                + " left fetch join ob.onionSushi"
                                + " left fetch join ob.onionKimchi"
                                + " left fetch join ob.onionSoup"
                                + " left fetch join ob.onionGrilled"
                                + " where ob.id = :id",
                        OnionBook.class)
                .setParameter("id", id)
                .getSingleResult();
    }


    @Override
    public OnionBook findByMemberId(Long memberId) {
        return em.createQuery("select ob from OnionBook ob"
                                + " left fetch join ob.onionGgang"
                                + " left fetch join ob.onionRing"
                                + " left fetch join ob.onionRaw"
                                + " left fetch join ob.onionPilled"
                                + " left fetch join ob.onionFried"
                                + " left fetch join ob.onionPickle"
                                + " left fetch join ob.onionSushi"
                                + " left fetch join ob.onionKimchi"
                                + " left fetch join ob.onionSoup"
                                + " left fetch join ob.onionGrilled"
                                + " where ob.member = :memberId",
                        OnionBook.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

}
