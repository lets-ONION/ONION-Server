package site.lets_onion.lets_onionApp.repository.onionBook;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import site.lets_onion.lets_onionApp.domain.onionBook.OnionType;

@Repository
public class OnionTypeRepositoryImpl implements OnionTypeRepository {

    @PersistenceContext
    private EntityManager em;

//    @Override
//    public OnionType findByOnionName(String name) {
//        return em.createQuery("select ot from OnionType ot"
//                                + " where ot.onionName = :onionName",
//                        OnionType.class)
//                .setParameter("onionName", name)
//                .getSingleResult();
//    }

}
