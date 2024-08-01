package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionBook;

import java.util.Optional;

@Repository
public class OnionBookRepositoryImpl implements OnionBookRepository{

    @PersistenceContext
    private EntityManager em;

    @Override
    public OnionBook findById(Long id) {
        return em.createQuery("select ob from OnionBook ob"
                                    + " where ob.id = :id",
                            OnionBook.class)
                    .setParameter("id", id)
                    .getSingleResult();
    }


    @Override
    public OnionBook findByMemberId(Long memberId) {
        return em.createQuery("select ob from OnionBook ob"
                                + " where ob.member.id = :memberId",
                        OnionBook.class)
                .setParameter("memberId", memberId)
                .getSingleResult();
    }

    @Override
    public OnionBook save(OnionBook onionBook) {
        em.persist(onionBook);
        return onionBook;
    }


}
